package com.github.poldroc.ioc.test.context;


import com.github.poldroc.ioc.context.JsonApplicationContext;
import com.github.poldroc.ioc.core.BeanFactory;
import com.github.poldroc.ioc.test.service.Person;
import org.junit.Test;

public class JsonApplicationContextTest {

    @Test
    public void simpleTest() {
        BeanFactory beanFactory = new JsonApplicationContext("person.json");
        Person person = beanFactory.getBean("person", Person.class);
        person.name();
    }

}
