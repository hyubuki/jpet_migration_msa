/*
 *    Copyright 2010-2022 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.jpetstore.service;

import org.mybatis.jpetstore.domain.Item;
import org.mybatis.jpetstore.domain.Order;
import org.mybatis.jpetstore.domain.OrderRetryStatus;
import org.mybatis.jpetstore.domain.Sequence;
import org.mybatis.jpetstore.dto.ProductRequestMessage;
import org.mybatis.jpetstore.dto.OrderProcessResult;
import org.mybatis.jpetstore.exception.OrderFailException;
import org.mybatis.jpetstore.exception.RetryUnknownException;
import org.mybatis.jpetstore.http.HttpFacade;
import org.mybatis.jpetstore.repository.LineItemRepository;
import org.mybatis.jpetstore.repository.OrderRepository;
import org.mybatis.jpetstore.repository.SequenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import jakarta.servlet.http.HttpSession;
import org.mybatis.jpetstore.domain.Account;
import org.mybatis.jpetstore.domain.Cart;
import java.util.*;

/**
 * The Class OrderService.
 *
 * @author Eduardo Macarron
 */
@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final SequenceRepository sequenceRepository;
  private final LineItemRepository lineItemRepository;
  private final HttpFacade httpFacade;
  private final KafkaTemplate<String, Object> kafkaTemplate;

  //  재요청 실패로 inventory update 여부를 알 수 없는 상태
  private static final String UNKNOWN = "unknown";
  // 주문 성공한 상태
  private static final String SUCCESS = "success";
  // 재요청을 성공하여 지난 주문이 실패했을을 알고 있는 상태
  private static final String FAIL = "fail";
  // 첫 주문 등의 이유로 지난 주문에 대한 내역이 없는 상태
  private static final String UNPROCESSED = "unprocessed";
  @Autowired
  public OrderService(OrderRepository orderRepository, SequenceRepository sequenceRepository,
                      LineItemRepository lineItemRepository, HttpFacade httpFacade, KafkaTemplate<String, Object> kafkaTemplate) {
    this.orderRepository = orderRepository;
    this.sequenceRepository = sequenceRepository;
    this.lineItemRepository = lineItemRepository;
    this.httpFacade = httpFacade;
    this.kafkaTemplate = kafkaTemplate;
  }

  /**
   * 계정과 장바구니 정보를 기반으로 새 주문을 생성합니다.
   *
   * @param account 주문을 요청한 사용자 계정
   * @param cart    주문에 담길 장바구니
   * @return 초기화된 주문 객체
   */
  public Order createOrder(Account account, Cart cart) {
    Order order = new Order();
    order.initOrder(account, cart);
    order.setOrderId(getNextId("ordernum"));
    return order;
  }

  /**
   * 청구 정보 변경 시 주문 객체에 반영합니다.
   *
   * @param target 수정 대상 주문
   * @param source 사용자가 입력한 주문 정보
   */
  public void updateBillingInfo(Order target, Order source) {
    target.setCardType(source.getCardType());
    target.setCreditCard(source.getCreditCard());
    target.setExpiryDate(source.getExpiryDate());
    target.setBillToFirstName(source.getBillToFirstName());
    target.setBillToLastName(source.getBillToLastName());
    target.setBillAddress1(source.getBillAddress1());
    target.setBillAddress2(source.getBillAddress2());
    target.setBillCity(source.getBillCity());
    target.setBillState(source.getBillState());
    target.setBillZip(source.getBillZip());
    target.setBillCountry(source.getBillCountry());
  }

  /**
   * 배송지 정보 변경 시 주문 객체에 반영합니다.
   *
   * @param target 수정 대상 주문
   * @param source 사용자가 입력한 주문 정보
   */
  public void updateShippingInfo(Order target, Order source) {
    target.setShipToFirstName(source.getShipToFirstName());
    target.setShipToLastName(source.getShipToLastName());
    target.setShipAddress1(source.getShipAddress1());
    target.setShipAddress2(source.getShipAddress2());
    target.setShipCity(source.getShipCity());
    target.setShipState(source.getShipState());
    target.setShipZip(source.getShipZip());
    target.setShipCountry(source.getShipCountry());
  }

  /**
   * 주문 진행 단계에 따라 주문을 갱신하고 다음 화면을 결정합니다.
   *
   * @param sessionOrder 세션에 저장된 주문
   * @param formOrder    사용자가 입력한 주문 정보
   * @param shippingAddressRequired 배송지 입력 단계 여부
   * @param confirmed    주문 최종 확인 여부
   * @param changeShipInfo 배송지 변경 여부
   * @param session      HTTP 세션
   * @return 처리 결과 DTO
   */
  public OrderProcessResult handleOrderProcess(Order sessionOrder, Order formOrder,
                                               boolean shippingAddressRequired,
                                               boolean confirmed,
                                               boolean changeShipInfo,
                                               HttpSession session) {
    if (sessionOrder == null) {
      return new OrderProcessResult("common/Error",
          "An error occurred processing your order (order was null).");
    }

    if (shippingAddressRequired) {
      updateBillingInfo(sessionOrder, formOrder);
      session.setAttribute("order", sessionOrder);
      return new OrderProcessResult("order/ShippingForm", null);
    }

    if (!confirmed) {
      if (changeShipInfo) {
        updateShippingInfo(sessionOrder, formOrder);
      }
      session.setAttribute("order", sessionOrder);
      return new OrderProcessResult("order/ConfirmOrder", null);
    }

    try {
      insertOrder(sessionOrder, session);
      session.removeAttribute("cart");
      return new OrderProcessResult("order/ViewOrder",
          "Thank you, your order has been submitted.");
    } catch (RetryUnknownException e) {
      return new OrderProcessResult("order/ConfirmOrder", null);
    } catch (OrderFailException e) {
      return new OrderProcessResult("common/Error", e.getMessage());
    }
  }

  /**
   * 주문을 생성하고 재고 업데이트를 요청합니다.
   *
   * @param order 생성할 주문
   */
  @Transactional
  public void insertOrder(Order order, HttpSession session) throws OrderFailException, RetryUnknownException {

      Optional<OrderRetryStatus> orderRetryStatus = orderRepository.findStatus(order.getOrderId());

      if (!orderRetryStatus.isPresent()){
          orderRepository.insertStatus(new OrderRetryStatus(order.getOrderId(), UNPROCESSED));
          orderRetryStatus = orderRepository.findStatus(order.getOrderId());
      }

      if(orderRetryStatus.get().getStatus().equals(SUCCESS)){
        return;
      }

      // Unknown 재요청 실패한 경우 -> 재요청 필요
      if (orderRetryStatus.get().getStatus().equals(UNKNOWN)){
        updateCommitSuccessCheck(order);
      }

    Map<String, Object> incrementPerItem = getIncrementAndItemsParam(order);
    boolean resp = httpFacade.updateInventoryQuantity(incrementPerItem,order.getOrderId());

    // 즉시 재요청 : 5xx error, Time-out 발생한 경우 (비정상 실패)
    if (!resp) {
      updateCommitSuccessCheck(order);
    }

    try{
      orderRepository.insert(order);
    }catch (Exception e){
      kafkaTemplate.send("product_compensation",incrementPerItem);
    }




    orderRepository.updateStatus(new OrderRetryStatus(order.getOrderId(), SUCCESS));
  }

  private static Map<String, Object> getIncrementAndItemsParam(Order order) {
    Map<String, Object> param = new HashMap<>();
    List<String> itemIds = new ArrayList<>();

    order.getLineItems().forEach(lineItem -> {
      String itemId = lineItem.getItemId();
      Integer increment = lineItem.getQuantity();
      param.put(itemId, increment);
      itemIds.add(itemId);
      // http 통신을 id마다 하지말고, 한번에 할 것
    });
    return param;
  }

  private void updateCommitSuccessCheck(Order sessionOrder) throws OrderFailException, RetryUnknownException {
    try{
      // 즉시 재요청 - 수량 변경 commit 성공 여부 확인
      Boolean isCommitSuccess = httpFacade.isInventoryUpdateCommitSuccess(sessionOrder.getOrderId());

      if (!isCommitSuccess) {
        // fail : Known_case2 : commit 실패한 경우 -> 실패 처리
        System.out.println("Known_case2_commitFail");
        orderRepository.updateStatus(new OrderRetryStatus(sessionOrder.getOrderId(), FAIL));
        throw new OrderFailException("주문 실패");
      }

    } catch(HttpServerErrorException serverError){
        // Unknown : 즉시 재요청에 server Error 발생, 트랜잭션 커밋 성공 여부를 알 수 없는 경우
        System.out.println("Unknown_case1_serverError");
        orderRepository.updateStatus(new OrderRetryStatus(sessionOrder.getOrderId(),UNKNOWN));
        throw new RetryUnknownException("서버 에러 발생");

      } catch( ResourceAccessException timeOut) {
        // Unknown : 즉시 재요청 자체를 실패 , 이또한 트랜잭션 커밋 성공 여부를 알 수 없음.
        System.out.println("Unknown_case2_ResourceAccessError");
        orderRepository.updateStatus(new OrderRetryStatus(sessionOrder.getOrderId(), UNKNOWN));
        throw new RetryUnknownException("리소스 접근 예외 발생 - time out");
      }
    System.out.println("문제 없음");
  }

  /**
   * 주문 상세 정보를 조회합니다.
   *
   * @param orderId 주문 ID
   * @return 주문 정보
   */
  @Transactional
  public Order getOrder(int orderId) {
    Order order = orderRepository.findById(orderId);
    order.setLineItems(lineItemRepository.findByOrderId(orderId));

    order.getLineItems().forEach(lineItem -> {
      Item item = httpFacade.getItem(lineItem.getItemId());
      item.setQuantity(httpFacade.getInventoryQuantity(lineItem.getItemId()));
      lineItem.setItem(item);
    });
    return order;
  }

  /**
   * 사용자 이름으로 주문 목록을 조회합니다.
   *
   * @param username 사용자 이름
   * @return 주문 목록
   */
  public List<Order> getOrdersByUsername(String username) {
    return orderRepository.findByUsername(username);
  }

  /**
   * 시퀀스에서 다음 값을 가져옵니다.
   *
   * @param name 시퀀스 이름
   * @return 다음 시퀀스 값
   */
  @Transactional
  public int getNextId(String name) {
    Sequence sequence = sequenceRepository.getSequence(new Sequence(name, -1));
    if (sequence == null) {
      throw new RuntimeException(
          "Error: A null sequence was returned from the database (could not get next " + name + " sequence).");
    }
    Sequence parameterObject = new Sequence(name, sequence.getNextId() + 1);
    sequenceRepository.updateSequence(parameterObject);
    return sequence.getNextId();
  }

}
