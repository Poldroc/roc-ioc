package com.github.poldroc.ioc.core.impl;

import com.github.poldroc.ioc.core.ListableBeanFactory;
import java.util.List;


public class DefaultListableBeanFactory extends DefaultBeanFactory implements ListableBeanFactory {

    @Override
    public <T> List<T> getBeans(Class<T> requiredType) {
        return super.getBeans(requiredType);
    }
}
