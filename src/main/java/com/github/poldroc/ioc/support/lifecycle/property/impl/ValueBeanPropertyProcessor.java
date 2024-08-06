package com.github.poldroc.ioc.support.lifecycle.property.impl;


import com.github.poldroc.ioc.core.BeanFactory;
import com.github.poldroc.ioc.model.PropertyArgDefinition;
import com.github.poldroc.ioc.support.converter.StringValueConverterFactory;
import com.github.poldroc.ioc.support.lifecycle.property.SingleBeanPropertyProcessor;
import com.github.poldroc.ioc.util.ClassUtil;
import com.github.poldroc.ioc.util.MethodUtil;

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

class ValueBeanPropertyProcessor implements SingleBeanPropertyProcessor {

    private static final ValueBeanPropertyProcessor INSTANCE = new ValueBeanPropertyProcessor();

    public static ValueBeanPropertyProcessor getInstance() {
        return INSTANCE;
    }


    @Override
    public void propertyProcessor(BeanFactory beanFactory, Object instance, PropertyArgDefinition propertyArgDefinition) {
        Class type = ClassUtil.getClass(propertyArgDefinition.getType());
        Object value = StringValueConverterFactory.convertValue(propertyArgDefinition.getValue(), type);
        MethodUtil.invokeSetterMethod(instance, propertyArgDefinition.getName(), value);

    }
}
