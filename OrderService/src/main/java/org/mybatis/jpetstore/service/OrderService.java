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

import com.sun.net.httpserver.Authenticator;
import org.mybatis.jpetstore.domain.Item;
import org.mybatis.jpetstore.domain.Order;
import org.mybatis.jpetstore.domain.OrderRetryStatus;
import org.mybatis.jpetstore.domain.Sequence;
import org.mybatis.jpetstore.dto.ProductRequestMessage;
import org.mybatis.jpetstore.exception.OrderFailException;
import org.mybatis.jpetstore.exception.RetryUnknownException;
import org.mybatis.jpetstore.http.HttpFacade;
import org.mybatis.jpetstore.mapper.LineItemMapper;
import org.mybatis.jpetstore.mapper.OrderMapper;
import org.mybatis.jpetstore.mapper.SequenceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * The Class OrderService.
 *
 * @author Eduardo Macarron
 */
@Service
public class OrderService {

  private final OrderMapper orderMapper;
  private final SequenceMapper sequenceMapper;
  private final LineItemMapper lineItemMapper;
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
  public OrderService(OrderMapper orderMapper, SequenceMapper sequenceMapper,
                      LineItemMapper lineItemMapper, HttpFacade httpFacade, KafkaTemplate<String, Object> kafkaTemplate) {
    this.orderMapper = orderMapper;
    this.sequenceMapper = sequenceMapper;
    this.lineItemMapper = lineItemMapper;
    this.httpFacade = httpFacade;
    this.kafkaTemplate = kafkaTemplate;
  }

  /**
   * Insert order.
   *
   * @param order
   *  the order
   */
  @Transactional
  public void insertOrder(Order order, HttpSession session) throws OrderFailException, RetryUnknownException {
    // 매개변수로 들어온 order도 사실 session order라는 사실 반영해서 수정 필요함.
    Order sessionOrder  = (Order) session.getAttribute("order");

    // 유저의 orderId가 있다면 OrderRetryStatus 상태 확인 (첫 주문이 아닌 경우)
    // 지난 주문 내역이 있는 경우
    if(sessionOrder != null){
      
      Optional<OrderRetryStatus> orderRetryStatus = orderMapper.getStatus(sessionOrder.getOrderId());

      if (!orderRetryStatus.isPresent()){
          orderMapper.insertStatus(new OrderRetryStatus(sessionOrder.getOrderId(), UNPROCESSED));
          orderRetryStatus = orderMapper.getStatus(sessionOrder.getOrderId());
      }

      // Unknown 재요청 실패한 경우 -> 재요청 필요
      if (orderRetryStatus.get().getStatus().equals(UNKNOWN)){
        Map<String, Object> param = getIncrementAndItemsParam(sessionOrder);
        updateCommitSuccessCheck(sessionOrder,param);
      }
    }

    Map<String, Object> param = getIncrementAndItemsParam(order);
    boolean resp = httpFacade.updateInventoryQuantity(param,order.getOrderId());
    System.out.println("updateInventoryQuantity:" + resp);

    // 즉시 재요청 : 5xx error, Time-out 발생한 경우 (비정상 실패)
    if (!resp) {
      updateCommitSuccessCheck(sessionOrder, param);
    }

    orderMapper.updateStatus(new OrderRetryStatus(sessionOrder.getOrderId(), SUCCESS));

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

  private void updateCommitSuccessCheck(Order sessionOrder, Map<String, Object> param) throws OrderFailException, RetryUnknownException {
    try{
      // 즉시 재요청 - 수량 변경 commit 성공 여부 확인
      Boolean isCommitSuccess = httpFacade.isInventoryUpdateCommitSuccess(sessionOrder.getOrderId());

      // fail : Known_case1 : commit 성공한 경우 -> 보상 트랜잭션
      if(isCommitSuccess){
        kafkaTemplate.send("prod_compensation", param);
        orderMapper.insertStatus(new OrderRetryStatus(sessionOrder.getOrderId(),FAIL));
      }
      else if (!isCommitSuccess) {
        // fail : Known_case2 : commit 실패한 경우 -> 실패 처리
        orderMapper.updateStatus(new OrderRetryStatus(sessionOrder.getOrderId(), FAIL));
        throw new OrderFailException("주문 실패");
      }
    } catch(HttpServerErrorException serverError){
        // Unknown : 즉시 재요청에 server Error 발생, 트랜잭션 커밋 성공 여부를 알 수 없는 경우
        orderMapper.updateStatus(new OrderRetryStatus(sessionOrder.getOrderId(),UNKNOWN));
        throw new RetryUnknownException("서버 에러 발생");
      } catch( ResourceAccessException timeOut) {
        // Unknown : 즉시 재요청 자체를 실패 , 이또한 트랜잭션 커밋 성공 여부를 알 수 없음.
        orderMapper.updateStatus(new OrderRetryStatus(sessionOrder.getOrderId(), UNKNOWN));
        throw new RetryUnknownException("리소스 접근 예외 발생 - time out");
      }
  }

  /**
   * Gets the order.
   *
   * @param orderId
   *          the order id
   *
   * @return the order
   */
  @Transactional
  public Order getOrder(int orderId) {
    Order order = orderMapper.getOrder(orderId);
    order.setLineItems(lineItemMapper.getLineItemsByOrderId(orderId));

    order.getLineItems().forEach(lineItem -> {
      Item item = httpFacade.getItem(lineItem.getItemId());
      item.setQuantity(httpFacade.getInventoryQuantity(lineItem.getItemId()));
      lineItem.setItem(item);
    });

    return order;
  }

  /**
   * Gets the orders by username.
   *
   * @param username
   *          the username
   *
   * @return the orders by username
   */
  public List<Order> getOrdersByUsername(String username) {
    return orderMapper.getOrdersByUsername(username);
  }

  /**
   * Gets the next id.
   *
   * @param name
   *          the name
   *
   * @return the next id
   */
  public int getNextId(String name) {
    Sequence sequence = sequenceMapper.getSequence(new Sequence(name, -1));
    if (sequence == null) {
      throw new RuntimeException(
          "Error: A null sequence was returned from the database (could not get next " + name + " sequence).");
    }
    Sequence parameterObject = new Sequence(name, sequence.getNextId() + 1);
    sequenceMapper.updateSequence(parameterObject);
    return sequence.getNextId();
  }

}
