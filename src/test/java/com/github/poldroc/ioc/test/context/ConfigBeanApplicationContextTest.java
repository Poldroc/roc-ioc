package com.github.poldroc.ioc.test.context;

import com.github.poldroc.ioc.context.AnnotationApplicationContext;
import com.github.poldroc.ioc.core.BeanFactory;
import com.github.poldroc.ioc.test.config.AppBeanConfig;
import com.github.poldroc.ioc.test.service.WeightApple;
import org.junit.Assert;
import org.junit.Test;

public class ConfigBeanApplicationContextTest {

    /**
     * 实现最基本的 Config-bean 测试
     */
    @Test
    public void weightAppleTest() {
        BeanFactory beanFactory = new AnnotationApplicationContext(AppBeanConfig.class);
        WeightApple apple = beanFactory.getBean("weightApple", WeightApple.class);

        Assert.assertEquals("10", apple.getWeight());
    }

}
