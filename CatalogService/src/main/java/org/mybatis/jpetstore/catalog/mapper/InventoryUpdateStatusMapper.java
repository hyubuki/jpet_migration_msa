package org.mybatis.jpetstore.catalog.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.jpetstore.common.domain.InventoryUpdateStatus;

import java.util.Optional;

@Mapper
public interface InventoryUpdateStatusMapper {
    Optional<InventoryUpdateStatus> getInventoryUpdateStatusByOrderId(Integer orderid);
    void insertInventoryUpdateStatus(InventoryUpdateStatus inventoryUpdateStatus);
    void deleteInventoryUpdateStatusByOrderId(Integer orderid);
}
