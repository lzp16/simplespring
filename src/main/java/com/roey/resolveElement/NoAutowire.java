package com.roey.resolveElement;

/**
 * Created by LiZhanPing on 2018/9/5.
 */
public class NoAutowire implements AutoWire {

    private String value;

    public NoAutowire(String value) {
        this.value=value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
