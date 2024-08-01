package com.github.poldroc.ioc.core.impl;

import com.github.poldroc.ioc.core.ListableBeanFactory;
import com.github.poldroc.ioc.exception.IocRuntimeException;
import com.github.poldroc.ioc.model.BeanDefinition;
import com.github.poldroc.ioc.util.ArgUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DefaultListableBeanFactory extends DefaultBeanFactory implements ListableBeanFactory {

    @Override
    public <T> List<T> getBeans(Class<T> requiredType) {
        ArgUtil.notNull(requiredType, "requiredType");

        Set<String> beanNames = super.getBeanNames(requiredType);
        if (beanNames.isEmpty()) {
            throw new IocRuntimeException(requiredType + " bean names is empty!");
        }

        List<T> beans = new ArrayList<>();
        for (String name : beanNames) {
            beans.add(DefaultListableBeanFactory.super.getBean(name, requiredType));
        }
        return beans;

    }
}
