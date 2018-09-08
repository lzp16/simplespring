package com.roey.setInput;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by LiZhanPing on 2018/9/8.
 */
public interface PropertyHandler {
    Map<String,Method> getSetterMethodMap(Object obj);

    void executeMethod(Object obj, Object setterArg, Method method);

    void setProperties(Object obj, Map<String, Object> propertiesMap);
}
