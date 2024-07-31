package com.github.poldroc.ioc.model;
/**
 * 对象定义属性
 * @author Poldroc
 * @date 2024/7/31
 */

public interface BeanDefinition {

    /**
     * 名称
     * @return 名称
     */
    String getName();

    /**
     * 设置名称
     * @param name 名称
     */
    void setName(final String name);

    /**
     * 类名称
     * @return 类名称
     */
    String getClassName();

    /**
     * 设置类名称
     * @param className 类名称
     */
    void setClassName(final String className);

}
