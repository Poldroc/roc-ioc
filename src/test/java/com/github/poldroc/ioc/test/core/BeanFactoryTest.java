package com.github.poldroc.ioc.test.core;

import com.github.poldroc.ioc.context.JsonApplicationContext;
import com.github.poldroc.ioc.core.BeanFactory;
import com.github.poldroc.ioc.test.service.Person;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * bean 工厂测试类
 * @author Poldroc
 * @date 2024/8/1
 */

public class BeanFactoryTest {

    /**
     * bean 工厂
     */
    private static final BeanFactory BEAN_FACTORY = new JsonApplicationContext("person.json");


    /**
     * 测试
     */
    @Test
    public void getBeanByNameTest() {
        BeanFactory beanFactory = new JsonApplicationContext("person.json");
        Person person = (Person) beanFactory.getBean("person");
        person.name();
    }

    /**
     * 测试
     */
    @Test
    public void getBeanByNameTypeTest() {
        Person person = BEAN_FACTORY.getBean("person", Person.class);
        person.name();
    }

    /**
     * 测试
     */
    @Test
    public void containsBeanTest() {
        Assert.assertTrue(BEAN_FACTORY.containsBean("person"));
        Assert.assertFalse(BEAN_FACTORY.containsBean("box"));
    }

    /**
     * 测试
     */
    @Test
    public void isTypeMatchTest() {
        Assert.assertTrue(BEAN_FACTORY.isTypeMatch("person", Person.class));
        Assert.assertFalse(BEAN_FACTORY.isTypeMatch("person", BeanFactory.class));
    }



}
