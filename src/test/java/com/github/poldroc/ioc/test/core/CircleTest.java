package com.github.poldroc.ioc.test.core;

import com.github.poldroc.ioc.context.JsonApplicationContext;
import com.github.poldroc.ioc.core.BeanFactory;
import com.github.poldroc.ioc.exception.IocRuntimeException;
import com.github.poldroc.ioc.test.service.ColorWeightApple;
import org.junit.Test;

public class CircleTest {

    /**
     * a-b 直接互相依赖
     */
    @Test(/*expected = IocRuntimeException.class*/)
    public void directCircleTest() {
        final BeanFactory beanFactory = new JsonApplicationContext("circle/direct-circle.json");
        ColorWeightApple apple = beanFactory.getBean("weightApple", ColorWeightApple.class);
    }

    /**
     * a-b
     * b-c
     * c-a
     *
     * 间接循环依赖
     */
    @Test(/*expected = IocRuntimeException.class*/)
    public void inDirectCircleTest() {
        final BeanFactory beanFactory = new JsonApplicationContext("circle/in-direct-circle.json");
        ColorWeightApple apple = beanFactory.getBean("weightApple", ColorWeightApple.class);
    }

}
