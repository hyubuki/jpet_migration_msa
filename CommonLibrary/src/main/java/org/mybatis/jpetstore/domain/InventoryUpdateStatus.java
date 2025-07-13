package org.mybatis.jpetstore.domain;
// Shared domain model used by multiple services
import java.io.Serializable;

public class InventoryUpdateStatus implements Serializable {
    private Integer orderid; // unique
    public InventoryUpdateStatus(Integer orderid) {
        this.orderid = orderid;
    }
    public Integer getOrderid() {
        return orderid;
    }
}

