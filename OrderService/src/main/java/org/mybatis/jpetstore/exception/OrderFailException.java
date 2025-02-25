package org.mybatis.jpetstore.exception;

public class OrderFailException extends Exception{
    public OrderFailException(String message){
        super(message);
    }
}
