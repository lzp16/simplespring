package com.roey.test;

/**
 * Created by LiZhanPing on 2018/8/27.
 */
public class Person {
    private String name;
    private String age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void info(){
        System.out.println("name:"+getName()+" age:"+getAge());
    }
}
