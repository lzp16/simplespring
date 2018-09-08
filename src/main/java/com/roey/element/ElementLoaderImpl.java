package com.roey.element;

import com.roey.readXml.DocumentHolder;
import com.roey.readXml.XmlDocumentHolder;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LiZhanPing on 2018/9/4.
 */
public class ElementLoaderImpl implements ElementLoader {

    private Map<String, Element> elementMap = new HashMap<>();

    public void addElements(Document doc) {
        //获取根节点的所有子节点
        List<Element> elements = doc.getRootElement().elements();
        elements.forEach(e -> elementMap.put(e.attributeValue("id"), e));
    }

    public Element getElement(String id) {
        return elementMap.get(id);
    }

    public Collection<Element> getAllElements() {
        return elementMap.values();
    }

    public static void main(String[] args) {
        String basePath = XmlDocumentHolder.class.getClassLoader().getResource(".").getPath().substring(1);
        DocumentHolder documentHolder = new XmlDocumentHolder();
        Document document = documentHolder.getDocument(basePath + "spring.xml");
        ElementLoader elementLoader = new ElementLoaderImpl();
        elementLoader.addElements(document);
        Element pad2 = elementLoader.getElement("pad2");
        System.out.println(pad2.attributeValue("class"));
    }
}
