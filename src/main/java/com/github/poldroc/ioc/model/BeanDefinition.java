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

    /**
     * 获取生命周期
     * @return 获取生命周期
     */
    String getScope();

    /**
     * 设置是否单例
     * @param scope 是否单例
     */
    void setScope(final String scope);

    /**
     * 是否为延迟加载
     * @return 是否
     */
    boolean isLazyInit();

    /**
     * 设置是否为延迟加载
     * @param lazyInit 是否
     */
    void setLazyInit(final boolean lazyInit);

}
