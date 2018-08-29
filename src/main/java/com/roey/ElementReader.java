package com.roey;

import org.dom4j.Element;

import java.util.List;

/**
 * Created by LiZhanPing on 2018/8/29.
 */
public interface ElementReader {
    boolean isLazy(Element element);

    boolean isSingleTon(Element element);

    AutoWire getAutoWire(Element element);

    List<Element> getConstructorElements(Element element);

    List<Element> getPropertyElements(Element element);

    String getProperty(Element element, String name);

    List<DataElement> getDataElements(Element element);

    List<ValueElement> getValueElement(Element element);
}
