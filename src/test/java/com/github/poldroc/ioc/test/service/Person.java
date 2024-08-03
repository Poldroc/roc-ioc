package com.github.poldroc.ioc.test.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class Person {

    @PostConstruct
    public void init() {
        System.out.println("Person init");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Person destroy");
    }

    public void name() {
        System.out.println("My name is Poldroc.");
    }

}
