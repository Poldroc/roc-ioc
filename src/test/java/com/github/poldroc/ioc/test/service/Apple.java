package com.github.poldroc.ioc.test.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class Apple {

    @PostConstruct
    public void init() {
        System.out.println("Apple init");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Apple destroy");
    }

    public void color() {
        System.out.println("Apple color: red. ");
    }

}
