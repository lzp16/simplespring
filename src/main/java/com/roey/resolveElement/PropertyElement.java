package com.roey.resolveElement;

/**
 * Created by LiZhanPing on 2018/9/5.
 */
public class PropertyElement {

    private String name;
    private DataElement dataElement;

    public PropertyElement(String name, DataElement dataElement) {
        this.name = name;
        this.dataElement = dataElement;
    }

    public String getName() {
        return name;
    }

    public DataElement getDataElement() {
        return dataElement;
    }
}
