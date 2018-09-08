package com.roey.test;

import com.roey.ioc.ApplicationContext;
import com.roey.ioc.ClassPathXmlApplicationContext;

/**
 * Created by LiZhanPing on 2018/9/9.
 */
public class SimpleSpringTest {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        Person person = (Person)applicationContext.getBean("person");
        System.out.println(person.getAge());
    }
}
