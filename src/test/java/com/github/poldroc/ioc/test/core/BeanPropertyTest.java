package com.github.poldroc.ioc.test.core;

import com.github.poldroc.ioc.context.JsonApplicationContext;
import com.github.poldroc.ioc.core.BeanFactory;

import com.github.poldroc.ioc.test.model.User;
import org.junit.Test;


public class BeanPropertyTest {

    /**
     * 对象属性测试
     */
    @Test
    public void beanPropertyTest() {
        BeanFactory beanFactory = new JsonApplicationContext("property/user-property.json");
        User user = beanFactory.getBean("user", User.class);
        System.out.println(user);
    }

}
