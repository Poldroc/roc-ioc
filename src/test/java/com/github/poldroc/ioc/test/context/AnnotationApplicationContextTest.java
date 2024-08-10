package com.github.poldroc.ioc.test.context;

import com.github.poldroc.ioc.context.AnnotationApplicationContext;
import com.github.poldroc.ioc.core.BeanFactory;
import com.github.poldroc.ioc.test.config.AppConfig;
import org.junit.Assert;
import org.junit.Test;

public class AnnotationApplicationContextTest {

    /**
     * 实现最基本的 Config 测试
     */
    @Test
    public void simpleTest() {
        BeanFactory beanFactory = new AnnotationApplicationContext(AppConfig.class);
        AppConfig config = beanFactory.getBean("appConfig", AppConfig.class);

        Assert.assertEquals("app config", config.name());
    }

}
