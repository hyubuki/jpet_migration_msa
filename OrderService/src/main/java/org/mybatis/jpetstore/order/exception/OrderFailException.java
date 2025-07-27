package org.mybatis.jpetstore.order.exception;

public class OrderFailException extends Exception{
    public OrderFailException(String message){
        super(message);
    }
}
