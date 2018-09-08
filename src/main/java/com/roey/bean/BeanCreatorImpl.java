package com.roey.bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LiZhanPing on 2018/9/7.
 */
public class BeanCreatorImpl implements BeanCreator {

    @Override
    public Object createBeanUseDefaultConstructor(String className) {
        Object object = null;
        try {
            Class clazz = Class.forName(className);
            object = clazz.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return object;
    }

    @Override
    public Object createBeanUseDefineConstructor(String className, List<Object> args) {
        Class[] argsClasses = getArgsClasses(args);
        try {
            Class clazz = Class.forName(className).getClass();
            Constructor constructor = findConstructor(clazz, argsClasses);
            if (constructor != null) {
                return constructor.newInstance(args);
            }
        } catch (NoSuchMethodException | ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Class[] getArgsClasses(List<Object> args) {
        List<Class> argsClassList = new ArrayList<>();
        args.forEach(e -> argsClassList.add(args.getClass()));
        Class[] argsClasses = new Class[argsClassList.size()];
        return argsClassList.toArray(argsClasses);
    }

    private Constructor findConstructor(Class clazz, Class[] argsClasses) throws NoSuchMethodException {
        Constructor constructor = getConstructor(clazz, argsClasses);
        if (constructor == null) {
            Constructor[] constructors = clazz.getConstructors();
            for (Constructor c : constructors) {
                Class[] constructorArgsClasses = c.getParameterTypes();
                if (constructorArgsClasses.length == argsClasses.length) {
                    if (isSameArgs(argsClasses, constructorArgsClasses)) {
                        return c;
                    }
                }
            }
        }
        return null;
    }

    private Constructor getConstructor(Class clazz, Class[] argsClass) throws SecurityException, NoSuchMethodException {
        try {
            return clazz.getConstructor(argsClass);
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isSameArgs(Class[] argsClass, Class[] constructorArgsClasses) {
        for (int i = 0; i < argsClass.length; i++) {
            try {
                argsClass[i].asSubclass(constructorArgsClasses[i]);
                if (i == (argsClass.length - 1)) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
        return false;
    }
}
