package com.github.poldroc.ioc.support.lifecycle.create;

import com.github.poldroc.ioc.core.BeanFactory;
import com.github.poldroc.ioc.model.BeanDefinition;
import com.github.poldroc.ioc.support.lifecycle.NewInstanceBean;
import com.github.poldroc.ioc.support.lifecycle.property.impl.DefaultBeanPropertyProcessor;

public class DefaultNewInstanceBean implements NewInstanceBean {

    /**
     * 单例
     */
    private static final DefaultNewInstanceBean INSTANCE = new DefaultNewInstanceBean();

    /**
     * 获取实例
     * @return 实例
     */
    public static DefaultNewInstanceBean getInstance() {
        return INSTANCE;
    }


    @Override
    public Object newInstance(BeanFactory beanFactory, BeanDefinition beanDefinition) {
        Object instance;

        // 工厂方法创建
        Object factoryMethodBean = FactoryMethodNewInstanceBean.getInstance()
                .newInstance(beanFactory, beanDefinition);
        if(factoryMethodBean != null) {
            instance = factoryMethodBean;
        } else {
            // 根据构造器创建
            instance = ConstructorNewInstanceBean.getInstance()
                    .newInstance(beanFactory, beanDefinition);
        }

        // 属性设置
        DefaultBeanPropertyProcessor.getInstance()
                .propertyProcessor(beanFactory, instance, beanDefinition.getPropertyArgList());

        // 回结果
        return instance;
    }
}
