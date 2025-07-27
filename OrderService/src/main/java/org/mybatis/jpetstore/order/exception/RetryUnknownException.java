package org.mybatis.jpetstore.order.exception;

public class RetryUnknownException extends Exception{
    public RetryUnknownException(String message) {
        super(message);
    }
}
