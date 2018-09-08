package com.roey.resolveElement;

/**
 * Created by LiZhanPing on 2018/9/5.
 */
public class RefElement implements DataElement {

    private Object value;

    public RefElement(String value) {
        this.value = value;
    }

    @Override
    public String getType() {
        return "ref";
    }

    @Override
    public Object getValue() {
        return value;
    }
}
