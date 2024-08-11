package com.github.poldroc.ioc.test.context;

import com.github.poldroc.ioc.context.AnnotationApplicationContext;
import com.github.poldroc.ioc.core.BeanFactory;
import com.github.poldroc.ioc.test.config.AppImportConfig;
import com.github.poldroc.ioc.test.service.WeightApple;
import org.junit.Assert;
import org.junit.Test;

public class ConfigImportAppCtxTest {

    /**
     * 添加 @Import 支持
     */
    @Test
    public void importTest() {
        BeanFactory beanFactory = new AnnotationApplicationContext(AppImportConfig.class);
        WeightApple weightApple = beanFactory.getBean("weightApple", WeightApple.class);

        Assert.assertEquals("10", weightApple.getWeight());
    }

}
