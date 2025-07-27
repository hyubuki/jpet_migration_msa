package org.mybatis.jpetstore.order.domain;

import java.io.Serializable;

public class OrderRetryStatus implements Serializable {
    private Integer orderid;
    private String status;

    public OrderRetryStatus(Integer orderid, String status) {
        this.orderid = orderid;
        this.status = status;
    }

    public Integer getOrderId() {
        return orderid;
    }

    public String getStatus() {
        return status;
    }
}
