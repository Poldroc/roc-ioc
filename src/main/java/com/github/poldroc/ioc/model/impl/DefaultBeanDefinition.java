package com.github.poldroc.ioc.model.impl;

import com.github.poldroc.ioc.model.BeanDefinition;
/**
 * 默认对象定义属性
 * @author Poldroc
 * @date 2024/7/31
 */

public class DefaultBeanDefinition implements BeanDefinition {

    private String name;

    private String className;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public void setClassName(String className) {
        this.className = className;
    }
}
