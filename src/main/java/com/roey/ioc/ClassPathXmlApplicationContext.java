package com.roey.ioc;

import com.roey.bean.BeanCreateException;
import com.roey.bean.BeanCreator;
import com.roey.bean.BeanCreatorImpl;
import com.roey.element.ElementLoader;
import com.roey.element.ElementLoaderImpl;
import com.roey.readXml.DocumentHolder;
import com.roey.readXml.XmlDocumentHolder;
import com.roey.resolveElement.*;
import com.roey.setInput.PropertyHandler;
import com.roey.setInput.PropertyHandlerImpl;
import org.dom4j.Document;
import org.dom4j.Element;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * Created by LiZhanPing on 2018/9/8.
 */
public class ClassPathXmlApplicationContext implements ApplicationContext {

    private DocumentHolder documentHolder = new XmlDocumentHolder();
    private ElementLoader elementLoader = new ElementLoaderImpl();
    private ElementReader elementReader = new ElementReaderImpl();

    private BeanCreator beanCreator = new BeanCreatorImpl();
    private PropertyHandler propertyHandler = new PropertyHandlerImpl();
    private Map<String, Object> beans = new HashMap<>();

    public ClassPathXmlApplicationContext(String... filePaths) {
        setUpElements(filePaths);
        createBeans();
    }

    private void createBeans() {
        Collection<Element> elements = elementLoader.getAllElements();
        for (Element element : elements) {
            boolean lazy = elementReader.isLazy(element);
            if (!lazy) {
                String id = element.attributeValue("id");
                Object bean = this.getBean(id);
                if (bean == null) {
                    handleSingleton(id);
                }
            }
        }
    }

    private Object handleSingleton(String id) {
        Object bean = createBean(id);
        if (isSingleTon(id)) {
            this.beans.put(id, bean);
        }
        return bean;
    }

    private Object createBean(String id) {
        //1、检查配置文件中是否有该元素
        Element e = elementLoader.getElement(id);
        if (e == null) {
            try {
                throw new BeanCreateException("element not found" + id);
            } catch (BeanCreateException e1) {
                e1.printStackTrace();
            }
        }
        //2、配置文件中存在该元素就创建
        Object instance = instance(e);
        System.out.println("创建bean" + id);
        System.out.println("该bean的对象是" + instance);
        //3、初始化属性
        AutoWire autoWire = elementReader.getAutoWire(e);
        if (autoWire instanceof ByNameAutowire) {
            // 使用名称自动装配
            autowireByName(instance);
        } else if (autoWire instanceof NoAutowire) {
            // 显示注入
            setterInject(instance, e);
        }
        return instance;
    }

    private Object instance(Element e) {
        String className = elementReader.getAttribute(e, "class");
        List<Element> constructorElements = elementReader.getConstructorArgElements(e);
        if (constructorElements.size() == 0) {
            return beanCreator.createBeanUseDefaultConstructor(className);
        } else {
            List<Object> args = getConstructArgs(e);
            return beanCreator.createBeanUseDefineConstructor(className, args);
        }
    }

    private List<Object> getConstructArgs(Element e) {
        List<DataElement> datas = elementReader.getConstructorArgValue(e);
        List<Object> result = new ArrayList<>();
        for (DataElement d : datas) {
            if (d instanceof ValueElement) {
                result.add(d.getValue());
            } else if (d instanceof RefElement) {
                String refid = (String) d.getValue();
                result.add(this.getBean(refid));
            }
        }
        return result;
    }

    private void autowireByName(Object obj) {
        Map<String, Method> methods = propertyHandler.getSetterMethodMap(obj);
        for (String methodName : methods.keySet()) {
            Element e = elementLoader.getElement(methodName);
            if (e == null) {
                continue;
            }
            Object refName = this.getBean(methodName);
            Method method = methods.get(methodName);
            propertyHandler.executeMethod(obj, refName, method);
        }
    }

    private void setterInject(Object obj, Element e) {
        List<PropertyElement> properties = elementReader.getPropertyValue(e);
        Map<String, Object> propertiesMap = getPropertyArgs(properties);
        propertyHandler.setProperties(obj, propertiesMap);
    }

    private Map<String, Object> getPropertyArgs(List<PropertyElement> properties) {
        Map<String, Object> result = new HashMap<>();
        for (PropertyElement p : properties) {
            DataElement de = p.getDataElement();
            if (de instanceof RefElement) {
                result.put(p.getName(), this.getBean((String) de.getValue()));
            } else if (de instanceof ValueElement) {
                result.put(p.getName(), de.getValue());
            }
        }
        return result;
    }

    private void setUpElements(String[] xmlPaths) {
        URL classPathUrl = ClassPathXmlApplicationContext.class.getClassLoader().getResource(".");
        String classpath;
        try {
            classpath = java.net.URLDecoder.decode(classPathUrl != null ? classPathUrl.getPath() : "", "utf-8");
            for (String path : xmlPaths) {
                Document doc = documentHolder.getDocument(classpath + path);
                elementLoader.addElements(doc);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getBean(String id) {
        Object bean = this.beans.get(id);
        if (bean == null) {
            bean = handleSingleton(id);
        }
        return bean;
    }

    @Override
    public boolean containsBean(String id) {
        Element element = elementLoader.getElement(id);
        return element != null;
    }

    @Override
    public boolean isSingleTon(String id) {
        Element element = elementLoader.getElement(id);
        return elementReader.isSingleTon(element);
    }
}
