package com.roey.setInput;

import com.roey.bean.BeanCreateException;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LiZhanPing on 2018/9/8.
 */
public class PropertyHandlerImpl implements PropertyHandler {

    @Override
    public Map<String, Method> getSetterMethodMap(Object obj) {
        Map<String, Method> methodMap = new HashMap<>();
        Method[] methods = obj.getClass().getMethods();
        for (Method method : methods) {
            String name = method.getName();
            if (name.startsWith("set")) {
                methodMap.put(getMethodNameWithOutSet(name), method);
            }
        }
        return methodMap;
    }

    private String getMethodNameWithOutSet(String methodname) {
        String propertyName = methodname.replace("set", "");
        String firstWord = propertyName.substring(0, 1);
        String lowerFirstWord = firstWord.toLowerCase();
        return propertyName.replaceFirst(firstWord, lowerFirstWord);
    }

    @Override
    public void executeMethod(Object obj, Object setterArg, Method method) {
        try {
            if (isMethodArgs(method, setterArg.getClass())) {
                method.invoke(obj, setterArg);
            }
        } catch (Exception e) {
            try {
                throw new BeanCreateException("autoWire exception" + e.getMessage());
            } catch (BeanCreateException e1) {
                e1.printStackTrace();
            }
        }
    }

    private boolean isMethodArgs(Method method, Class class1) {
        Class[] c = method.getParameterTypes();
        if (c.length == 1) {
            try {
                class1.asSubclass(c[0]);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    @Override
    public void setProperties(Object obj, Map<String, Object> propertiesMap) {
        Map<String, Method> setterMethodMap = getSetterMethodMap(obj);
        propertiesMap.forEach( (k,v)->executeMethod(obj,v,setterMethodMap.get(k)));
    }
}
