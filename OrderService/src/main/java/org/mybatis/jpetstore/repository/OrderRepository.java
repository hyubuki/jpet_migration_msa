package org.mybatis.jpetstore.repository;

import org.mybatis.jpetstore.domain.Order;
import org.mybatis.jpetstore.domain.OrderRetryStatus;
import org.mybatis.jpetstore.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 주문 정보에 접근하는 레포지터리입니다.
 */
@Repository
public class OrderRepository {

    private final OrderMapper mapper;

    @Autowired
    public OrderRepository(OrderMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 사용자 이름으로 주문 목록을 조회합니다.
     *
     * @param username 사용자 이름
     * @return 주문 목록
     */
    public List<Order> findByUsername(String username) {
        return mapper.getOrdersByUsername(username);
    }

    /**
     * 주문 ID로 주문을 조회합니다.
     *
     * @param orderId 주문 ID
     * @return 주문 정보
     */
    public Order findById(int orderId) {
        return mapper.getOrder(orderId);
    }

    /**
     * 주문을 저장합니다.
     *
     * @param order 저장할 주문
     */
    public void insert(Order order) {
        mapper.insertOrder(order);
    }

    /**
     * 주문 상태를 저장합니다.
     *
     * @param status 상태 정보
     */
    public void insertStatus(OrderRetryStatus status) {
        mapper.insertStatus(status);
    }

    /**
     * 주문 상태를 업데이트합니다.
     *
     * @param status 변경할 상태
     */
    public void updateStatus(OrderRetryStatus status) {
        mapper.updateStatus(status);
    }

    /**
     * 주문 상태를 조회합니다.
     *
     * @param orderId 주문 ID
     * @return 상태 정보
     */
    public Optional<OrderRetryStatus> findStatus(int orderId) {
        return mapper.getStatus(orderId);
    }
}
