package org.mybatis.jpetstore.domain;
import java.io.Serializable;

public class InventoryUpdateStatus implements Serializable {
    private String orderId; // unique
    public InventoryUpdateStatus(String orderId) {
        this.orderId = orderId;
    }
    public String getOrderId() {
        return orderId;
    }
    @Override
    public String toString(){
        return "orderId:" + this.orderId;
    }
}

