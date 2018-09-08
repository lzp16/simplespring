package com.roey.resolveElement;

/**
 * Created by LiZhanPing on 2018/9/5.
 */
public class ValueElement implements DataElement{

    private Object value;

    public ValueElement(Object value) {
        this.value = value;
    }

    @Override
    public String getType() {
        return "value";
    }

    @Override
    public Object getValue() {
        return value;
    }
}
