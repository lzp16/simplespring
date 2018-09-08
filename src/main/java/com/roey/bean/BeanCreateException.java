package com.roey.bean;

/**
 * Created by LiZhanPing on 2018/9/8.
 */
public class BeanCreateException extends Exception {

    public BeanCreateException(String message) {
        super(message);
    }

    public BeanCreateException(String message, Throwable cause) {
        super(message, cause);
    }
}
