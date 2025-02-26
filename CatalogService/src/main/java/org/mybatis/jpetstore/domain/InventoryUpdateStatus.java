package org.mybatis.jpetstore.domain;
import java.io.Serializable;

public class InventoryUpdateStatus implements Serializable {
    private Integer orderId; // unique
    public InventoryUpdateStatus(Integer orderId) {
        this.orderId = orderId;
    }
    public Integer getOrderId() {
        return orderId;
    }
}

