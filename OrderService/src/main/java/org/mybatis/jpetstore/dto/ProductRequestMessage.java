package org.mybatis.jpetstore.dto;

public class ProductRequestMessage{
    private Integer orderId;
    private Integer retryCount;

    public ProductRequestMessage(Integer orderId, Integer retryCount){
        this.orderId = orderId;
        this.retryCount = retryCount;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public Integer getRetryCount() {
        return retryCount;
    }
}