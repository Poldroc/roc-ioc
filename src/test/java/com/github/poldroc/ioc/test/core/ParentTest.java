package com.github.poldroc.ioc.test.core;

import com.github.poldroc.ioc.context.JsonApplicationContext;
import com.github.poldroc.ioc.core.BeanFactory;
import com.github.poldroc.ioc.test.model.User;
import org.junit.Assert;
import org.junit.Test;

public class ParentTest {

    @Test
    public void parentNameTest() {
        final BeanFactory beanFactory = new JsonApplicationContext("parent/user-parent.json");
        User copyUser = beanFactory.getBean("copyUser", User.class);
        Assert.assertEquals("roc", copyUser.getName());
    }

}
