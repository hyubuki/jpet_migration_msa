package org.mybatis.jpetstore.exception;

public class RetryUnknownException extends Exception{
    public RetryUnknownException(String message) {
        super(message);
    }
}
