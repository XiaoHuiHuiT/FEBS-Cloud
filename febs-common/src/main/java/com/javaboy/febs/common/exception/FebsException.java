package com.javaboy.febs.common.exception;

/**
 * FebsException为FEBS系统通用业务异常
 */
public class FebsException extends Exception{

    private static final long serialVersionUID = -6916154462432027437L;

    public FebsException(String message){
        super(message);
    }
}