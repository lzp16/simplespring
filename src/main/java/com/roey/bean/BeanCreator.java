package com.roey.bean;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created by LiZhanPing on 2018/9/7.
 */
public interface BeanCreator {

    Object createBeanUseDefaultConstructor(String className);

    Object createBeanUseDefineConstructor(String className, List<Object> args);
}