package com.roey.resolveElement;

import com.roey.resolveElement.AutoWire;
import com.roey.resolveElement.DataElement;
import com.roey.resolveElement.PropertyElement;
import org.dom4j.Element;

import java.util.List;

/**
 * Created by LiZhanPing on 2018/8/29.
 */
public interface ElementReader {
    boolean isLazy(Element element);

    boolean isSingleTon(Element element);

    AutoWire getAutoWire(Element element);

    List<DataElement> getConstructorArgValue(Element element);

    List<Element> getConstructorArgElements(Element element);

    List<PropertyElement> getPropertyValue(Element element);

    List<Element> getPropertyElements(Element element);

    String getAttribute(Element element, String name);
}
