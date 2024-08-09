package com.github.poldroc.ioc.support.lifecycle.processor;

public interface BeanPostProcessor extends PostProcessor {

    /**
     * 属性设置之前
     * @param beanName 对象名称
     * @param instance 实例
     * @return 结果
     */
    Object beforePropertySet(final String beanName, final Object instance);

    /**
     * 属性设置之后
     * @param beanName 对象名称
     * @param instance 实例
     * @return 结果
     */
    Object afterPropertySet(final String beanName, final Object instance);

}
