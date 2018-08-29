package com.roey;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LiZhanPing on 2018/8/29.
 */
public class XmlDocumentHolder implements DocumentHolder {

    //问题1、线程安全 2、为什么存储document 3、是否应该支持热加载
    private Map<String, Document> documentMap = new HashMap<String, Document>();

    public Document getDocument(String filePath) {

        Document doc = documentMap.get(filePath);
        if (doc == null) {
            documentMap.put(filePath, readDocument(filePath));
        }
        return documentMap.get(filePath);
    }

    private Document readDocument(String filePath) {
        Document doc = null;
        SAXReader reader = new SAXReader(true);
        reader.setEntityResolver(new IocEntityResolver());
        File xmlFile = new File(filePath);
        try {
            doc = reader.read(xmlFile);
        } catch (DocumentException e) {
            System.out.println("read xml file failed, file path : " + filePath);
            e.printStackTrace();
        }
        return doc;
    }
}
