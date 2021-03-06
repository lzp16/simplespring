package com.roey.element;

import org.dom4j.Document;
import org.dom4j.Element;

import java.util.Collection;

/**
 * Created by LiZhanPing on 2018/8/29.
 */
public interface ElementLoader {
    void addElements(Document doc);

    Element getElement(String id);

    Collection<Element> getAllElements();
}
