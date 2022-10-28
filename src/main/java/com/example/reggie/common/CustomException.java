package com.example.reggie.common;

import org.omg.CORBA.PUBLIC_MEMBER;

/**
 * 自定义业务异常
 */
public class CustomException extends RuntimeException{
    public CustomException (String msg){
        super(msg);
    }
}
