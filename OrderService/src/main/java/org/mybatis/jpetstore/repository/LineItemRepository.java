package org.mybatis.jpetstore.repository;

import org.mybatis.jpetstore.domain.LineItem;
import org.mybatis.jpetstore.mapper.LineItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 주문 항목에 대한 데이터 접근을 제공합니다.
 */
@Repository
public class LineItemRepository {

    private final LineItemMapper mapper;

    @Autowired
    public LineItemRepository(LineItemMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 주문 ID에 해당하는 모든 주문 항목을 조회합니다.
     *
     * @param orderId 주문 ID
     * @return 주문 항목 목록
     */
    public List<LineItem> findByOrderId(int orderId) {
        return mapper.getLineItemsByOrderId(orderId);
    }

    /**
     * 주문 항목을 저장합니다.
     *
     * @param lineItem 저장할 항목
     */
    public void insert(LineItem lineItem) {
        mapper.insertLineItem(lineItem);
    }
}
