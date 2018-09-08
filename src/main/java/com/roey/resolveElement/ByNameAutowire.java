package com.roey.resolveElement;

/**
 * Created by LiZhanPing on 2018/9/5.
 */
public class ByNameAutowire implements AutoWire {

    private String value;

    public ByNameAutowire(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
