package com.roey.resolveElement;

import com.roey.element.ElementLoaderImpl;
import com.roey.readXml.DocumentHolder;
import com.roey.element.ElementLoader;
import com.roey.readXml.XmlDocumentHolder;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiZhanPing on 2018/9/5.
 */
public class ElementReaderImpl implements ElementReader {
    @Override
    public boolean isSingleTon(Element element) {
        return Boolean.valueOf(getAttribute(element, "singleTon"));
    }

    @Override
    public boolean isLazy(Element element) {
        String lazy = getAttribute(element, "lazy-init");
        if ("".equals(lazy)) {
            return Boolean.valueOf(getAttribute(element.getParent(), "default-lazy-init"));
        } else {
            return "true".equals(lazy);
        }
    }

    @Override
    public AutoWire getAutoWire(Element element) {
        String autowire = getAttribute(element, "autowire");
        String parentAutowire = getAttribute(element.getParent(), "default-autowire");
        parentAutowire = "".equals(parentAutowire) ? "no" : parentAutowire;
        if (autowire == null) {
            if ("byName".equals(parentAutowire)) {
                return new ByNameAutowire(parentAutowire);
            } else {
                return new NoAutowire(parentAutowire);
            }
        } else if ("byName".equals(autowire)) {
            return new NoAutowire(autowire);
        } else {
            return new NoAutowire(autowire);
        }
    }

    @Override
    public List<DataElement> getConstructorArgValue(Element element) {
        List<DataElement> results = new ArrayList<>();
        getConstructorArgElements(element).forEach(e -> results.add(getDataElement(e)));
        return results;
    }

    @Override
    public List<Element> getConstructorArgElements(Element element) {
        List<Element> results = new ArrayList<>();
        ((List<Element>) element.elements()).forEach(e -> {
            if ("constructor-arg".equals(e.getName())) {
                results.add(e);
            }
        });
        return results;
    }

    @Override
    public List<PropertyElement> getPropertyValue(Element element) {
        List<PropertyElement> results = new ArrayList<>();
        getPropertyElements(element).forEach(e -> {
            String name = getAttribute(e, "name");
            DataElement value = getDataElement(e);
            PropertyElement pe = new PropertyElement(name, value);
            results.add(pe);
        });
        return results;
    }

    @Override
    public List<Element> getPropertyElements(Element element) {
        List<Element> results = new ArrayList<>();
        ((List<Element>) element.elements()).forEach(e -> {
            if ("property".equals(e.getName())) {
                results.add(e);
            }
        });
        return results;
    }

    private DataElement getDataElement(Element element) {
        if (getAttribute(element, "value") != null) {
            String type = getAttribute(element, "type");
            String data = getAttribute(element, "value");
            return new ValueElement(transferType(type, data));
        } else if (getAttribute(element, "ref") != null) {
            return new RefElement(getAttribute(element, "ref"));
        }
        return null;
    }

    private Object transferType(String className, String data) {
        if (isType(className, "Integer")) {
            return Integer.parseInt(data);
        } else {
            return data;
        }
    }

    private boolean isType(String className, String type) {
        if (className.indexOf(type) != -1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getAttribute(Element element, String name) {
        String value = element.attributeValue(name);
        return value==null?"":value;
    }

    public static void main(String[] args) {
        String basePath = XmlDocumentHolder.class.getClassLoader().getResource(".").getPath().substring(1);
        DocumentHolder documentHolder = new XmlDocumentHolder();
        Document document = documentHolder.getDocument(basePath + "spring.xml");
        ElementLoader elementLoader = new ElementLoaderImpl();
        elementLoader.addElements(document);
        Element pad2 = elementLoader.getElement("pad2");
        ElementReader elementReader = new ElementReaderImpl();
        System.out.println(elementReader.getAttribute(pad2, "id") + " lazy value:" + elementReader.isLazy(pad2));
        System.out.println(elementReader.getAttribute(pad2, "id") + " autowire value:" + elementReader.getAutoWire(pad2).getValue());
        System.out.println(elementReader.getAttribute(pad2, "id") + " singleTo value:" + elementReader.isSingleTon(pad2));
        System.out.println(elementReader.getAttribute(pad2, "id") + " constructor value:" + elementReader.getConstructorArgValue(pad2).toString());
        System.out.println(elementReader.getAttribute(pad2, "id") + " property value:" + elementReader.getPropertyValue(pad2));
    }
}
