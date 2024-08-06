package com.github.poldroc.ioc.test.core;


import com.github.poldroc.ioc.context.JsonApplicationContext;
import com.github.poldroc.ioc.core.BeanFactory;
import com.github.poldroc.ioc.test.service.ColorApple;
import com.github.poldroc.ioc.test.service.ColorWeightApple;
import org.junit.Test;

public class NewInstanceTest {

    /**
     * 测试
     */
    @Test
    public void factoryMethodAnnotationTest() throws InterruptedException {
        BeanFactory beanFactory = new JsonApplicationContext("create/colorApple.json");
        ColorApple apple = beanFactory.getBean("apple", ColorApple.class);
        System.out.println(apple);
    }

    /**
     * 构造器测试
     */
    @Test
    public void constructorTest() {
        BeanFactory beanFactory = new JsonApplicationContext("create/colorApple.json");
        ColorWeightApple apple = beanFactory.getBean("weightApple", ColorWeightApple.class);
        System.out.println(apple);
    }

}
