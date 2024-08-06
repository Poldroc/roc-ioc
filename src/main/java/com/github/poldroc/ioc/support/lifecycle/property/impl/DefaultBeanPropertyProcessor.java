package com.github.poldroc.ioc.support.lifecycle.property.impl;

import com.github.poldroc.ioc.core.BeanFactory;
import com.github.poldroc.ioc.model.PropertyArgDefinition;
import com.github.poldroc.ioc.support.lifecycle.property.BeanPropertyProcessor;

import java.util.List;

/**
 * 对象属性设置类。
 * <p>
 * 主要分为两个部分：
 * <p>
 * （1）直接根据属性值设置
 * （2）根据引用属性设置
 *
 * @author Poldroc
 * @date 2024/8/6
 */
public class DefaultBeanPropertyProcessor implements BeanPropertyProcessor {

    private static final DefaultBeanPropertyProcessor INSTANCE = new DefaultBeanPropertyProcessor();

    public static DefaultBeanPropertyProcessor getInstance() {
        return INSTANCE;
    }

    @Override
    public void propertyProcessor(BeanFactory beanFactory, Object instance, List<PropertyArgDefinition> propertyArgList) {
        if (propertyArgList == null || propertyArgList.isEmpty() || instance == null) {
            return;
        }
        for (PropertyArgDefinition argDefinition : propertyArgList) {
            if (argDefinition == null) {
                continue;
            }
            final String ref = argDefinition.getRef();
            if (ref == null || ref.isEmpty()) {
                ValueBeanPropertyProcessor.getInstance().propertyProcessor(beanFactory, instance, argDefinition);
            } else {
                RefBeanPropertyProcessor.getInstance().propertyProcessor(beanFactory, instance, argDefinition);
            }
        }
    }

}
