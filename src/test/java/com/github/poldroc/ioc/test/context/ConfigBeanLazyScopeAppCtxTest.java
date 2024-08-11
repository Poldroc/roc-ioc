package com.github.poldroc.ioc.test.context;

import com.github.poldroc.ioc.context.AnnotationApplicationContext;
import com.github.poldroc.ioc.core.BeanFactory;
import com.github.poldroc.ioc.test.config.AppBeanLazyScopeConfig;
import com.github.poldroc.ioc.test.service.Apple;
import com.github.poldroc.ioc.test.service.WeightApple;
import org.junit.Assert;
import org.junit.Test;

public class ConfigBeanLazyScopeAppCtxTest {

    /**
     * 添加 scope lazy-init 测试
     */
    @Test
    public void weightAppleTest() {
        BeanFactory beanFactory = new AnnotationApplicationContext(AppBeanLazyScopeConfig.class);
        WeightApple weightAppleOne = beanFactory.getBean("weightApple", WeightApple.class);
        WeightApple weightAppleTwo = beanFactory.getBean("weightApple", WeightApple.class);
        Assert.assertSame(weightAppleOne, weightAppleTwo);

        Apple appleOne = beanFactory.getBean("apple", Apple.class);
        Apple appleTwo = beanFactory.getBean("apple", Apple.class);
        Assert.assertNotSame(appleOne, appleTwo);
    }

}
