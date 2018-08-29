package com.roey;

import org.dom4j.Document;

/**
 * Created by LiZhanPing on 2018/8/29.
 */
public interface DocumentHolder {
    Document getDocument(String filePath);
}
