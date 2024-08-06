package com.github.poldroc.ioc.model.impl;

import com.github.poldroc.ioc.model.BeanDefinition;
import com.github.poldroc.ioc.model.ConstructorArgDefinition;
import com.github.poldroc.ioc.model.PropertyArgDefinition;

import java.util.Collections;
import java.util.List;

/**
 * 默认对象定义属性
 *
 * @author Poldroc
 * @date 2024/7/31
 */

public class DefaultBeanDefinition implements BeanDefinition {

    private String name;

    private String className;

    private String scope;

    private boolean lazyInit;

    private String initialize;

    private String destroy;

    private String factoryMethod;

    private List<ConstructorArgDefinition> constructorArgList;

    private List<PropertyArgDefinition> propertyArgList;

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

    @Override
    public String getScope() {
        return scope;
    }

    @Override
    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public boolean isLazyInit() {
        return lazyInit;
    }

    @Override
    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    @Override
    public String getInitialize() {
        return initialize;
    }

    @Override
    public void setInitialize(String initialize) {
        this.initialize = initialize;
    }

    @Override
    public String getDestroy() {
        return destroy;
    }

    @Override
    public void setDestroy(String destroy) {
        this.destroy = destroy;
    }

    @Override
    public void setFactoryMethod(String factoryMethod) {
        this.factoryMethod = factoryMethod;
    }

    @Override
    public String getFactoryMethod() {
        return factoryMethod;
    }

    @Override
    public List<ConstructorArgDefinition> getConstructorArgList() {
        return constructorArgList;
    }

    @Override
    public void setConstructorArgList(List<ConstructorArgDefinition> constructorArgList) {
        this.constructorArgList = constructorArgList;
    }

    @Override
    public List<PropertyArgDefinition> getPropertyArgList() {
        return propertyArgList;
    }

    @Override
    public void setPropertyArgList(List<PropertyArgDefinition> propertyArgList) {
        this.propertyArgList = propertyArgList;
    }

}
