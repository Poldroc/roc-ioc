package com.github.poldroc.ioc.model.impl;

import com.github.poldroc.ioc.model.AnnotationBeanDefinition;

public class DefaultAnnotationBeanDefinition extends DefaultBeanDefinition implements AnnotationBeanDefinition {

    /**
     * 配置信息名称
     */
    private String configurationName;

    /**
     * 配置信息对象名称
     */
    private String configurationBeanMethod;

    @Override
    public String getConfigurationName() {
        return configurationName;
    }

    @Override
    public void setConfigurationName(String configurationName) {
        this.configurationName = configurationName;
    }

    @Override
    public String getConfigurationBeanMethod() {
        return configurationBeanMethod;
    }

    @Override
    public void setConfigurationBeanMethod(String configurationBeanMethod) {
        this.configurationBeanMethod = configurationBeanMethod;
    }

}

