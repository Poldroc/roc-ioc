package com.github.poldroc.ioc.support.lifecycle.create;

import com.github.poldroc.ioc.constant.enums.BeanSourceTypeEnum;
import com.github.poldroc.ioc.core.BeanFactory;
import com.github.poldroc.ioc.core.ListableBeanFactory;
import com.github.poldroc.ioc.model.BeanDefinition;
import com.github.poldroc.ioc.support.lifecycle.NewInstanceBean;
import com.github.poldroc.ioc.support.lifecycle.processor.BeanPostProcessor;
import com.github.poldroc.ioc.support.lifecycle.property.impl.DefaultBeanPropertyProcessor;

import java.util.List;

public class DefaultNewInstanceBean implements NewInstanceBean {

    /**
     * 单例
     */
    private static final DefaultNewInstanceBean INSTANCE = new DefaultNewInstanceBean();

    /**
     * 获取实例
     *
     * @return 实例
     */
    public static DefaultNewInstanceBean getInstance() {
        return INSTANCE;
    }


    @Override
    public Object newInstance(BeanFactory beanFactory, BeanDefinition beanDefinition) {
        Object instance;
        BeanSourceTypeEnum beanSourceType = beanDefinition.getBeanSourceType();

        // 工厂方法创建
        Object factoryMethodBean = FactoryMethodNewInstanceBean.getInstance()
                .newInstance(beanFactory, beanDefinition);
        if (factoryMethodBean != null) {
            instance = factoryMethodBean;
        } else if (BeanSourceTypeEnum.CONFIGURATION_BEAN.equals(beanSourceType)) {
            // config-bean
            instance = ConfigurationMethodBean.getInstance().newInstance(beanFactory, beanDefinition);
        } else {
            // 根据构造器创建
            instance = ConstructorNewInstanceBean.getInstance()
                    .newInstance(beanFactory, beanDefinition);
        }
        // 通知 BeanPostProcessor
        String beanName = beanDefinition.getName();
        ListableBeanFactory listableBeanFactory = (ListableBeanFactory) beanFactory;
        List<BeanPostProcessor> beanPostProcessorList = listableBeanFactory.getBeans(BeanPostProcessor.class);

        // 属性设置之前
        for (BeanPostProcessor processor : beanPostProcessorList) {
            instance = processor.beforePropertySet(beanName, instance);
        }

        // 属性设置
        DefaultBeanPropertyProcessor.getInstance()
                .propertyProcessor(beanFactory, instance, beanDefinition.getPropertyArgList());

        // 属性设置之后
        for (BeanPostProcessor processor : beanPostProcessorList) {
            instance = processor.afterPropertySet(beanName, instance);
        }

        // 回结果
        return instance;
    }
}
