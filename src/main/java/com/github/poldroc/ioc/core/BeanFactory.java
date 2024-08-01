package com.github.poldroc.ioc.core;

/**
 * bean 工厂接口
 *
 * @author Poldroc
 * @date 2024/7/31
 */

public interface BeanFactory {

    /**
     * 根据名称获取对应的实例信息
     *
     * @param beanName bean 名称
     * @return 对象信息
     */
    Object getBean(String beanName);

    /**
     * 获取指定类型的实现
     *
     * @param beanName     属性名称
     * @param requiredType 类型
     * @param <T>          泛型
     * @return 结果
     */
    <T> T getBean(String beanName, Class<T> requiredType);

    /**
     * 是否包含指定的 bean
     *
     * @param beanName bean 名称
     * @return 是否包含
     */
    boolean containsBean(final String beanName);

    /**
     * 指定的 bean 和需要的类型二者是否匹配。
     *
     * @param beanName     bean 名称
     * @param requiredType 需要的类型
     * @return 是否包含
     */
    boolean isTypeMatch(final String beanName, final Class requiredType);

    /**
     * 获取 bean 对应的类型
     *
     * @param beanName bean 名称
     * @return 类型信息
     * @see #getBean(String) 对应的类型
     */
    Class<?> getType(final String beanName);
}
