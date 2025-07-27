package org.mybatis.jpetstore.repository;

import org.mybatis.jpetstore.domain.InventoryUpdateStatus;
import org.mybatis.jpetstore.mapper.InventoryUpdateStatusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * {@link InventoryUpdateStatus} 엔티티에 접근하기 위한 레포지터리입니다.
 */
@Repository
public class InventoryUpdateStatusRepository {

    private final InventoryUpdateStatusMapper mapper;

    @Autowired
    public InventoryUpdateStatusRepository(InventoryUpdateStatusMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 주어진 주문 ID에 대한 상태를 조회합니다.
     *
     * @param orderId 주문 ID
     * @return 상태 정보
     */
    public Optional<InventoryUpdateStatus> findByOrderId(Integer orderId) {
        return mapper.getInventoryUpdateStatusByOrderId(orderId);
    }

    /**
     * 새로운 상태 레코드를 추가합니다.
     *
     * @param status 추가할 상태
     */
    public void insertStatus(InventoryUpdateStatus status) {
        mapper.insertInventoryUpdateStatus(status);
    }

    /**
     * 주어진 주문 ID의 상태 레코드를 삭제합니다.
     *
     * @param orderId 주문 ID
     */
    public void deleteByOrderId(Integer orderId) {
        mapper.deleteInventoryUpdateStatusByOrderId(orderId);
    }
}
