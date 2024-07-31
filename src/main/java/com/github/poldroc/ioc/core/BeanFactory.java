package com.github.poldroc.ioc.core;
/**
 * bean 工厂接口
 * @author Poldroc
 * @date 2024/7/31
 */

public interface BeanFactory {

    /**
     * 根据名称获取对应的实例信息
     * @param beanName bean 名称
     * @return 对象信息
     */
    Object getBean(String beanName);

    /**
     * 获取指定类型的实现
     * @param beanName 属性名称
     * @param requiredType 类型
     * @param <T> 泛型
     * @return 结果
     */
    <T> T getBean(String beanName, Class<T> requiredType);


}
