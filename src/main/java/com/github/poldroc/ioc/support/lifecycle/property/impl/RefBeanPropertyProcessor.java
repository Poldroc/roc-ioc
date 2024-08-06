package com.github.poldroc.ioc.support.lifecycle.property.impl;


import com.github.poldroc.ioc.core.BeanFactory;
import com.github.poldroc.ioc.model.PropertyArgDefinition;
import com.github.poldroc.ioc.support.lifecycle.property.SingleBeanPropertyProcessor;
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

class RefBeanPropertyProcessor implements SingleBeanPropertyProcessor {

    private static final RefBeanPropertyProcessor INSTANCE = new RefBeanPropertyProcessor();

    public static RefBeanPropertyProcessor getInstance() {
        return INSTANCE;
    }

    @Override
    public void propertyProcessor(BeanFactory beanFactory, Object instance,
                                  PropertyArgDefinition propertyArgDefinition) {
        Object ref = beanFactory.getBean(propertyArgDefinition.getRef());
        MethodUtil.invokeSetterMethod(instance, propertyArgDefinition.getName(), ref);
    }


}
