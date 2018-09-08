package com.roey.ioc;

/**
 * Created by LiZhanPing on 2018/8/29.
 */
public interface ApplicationContext{
    Object getBean(String id);

    boolean containsBean(String id);

    boolean isSingleTon(String id);
}
