package com.github.poldroc.ioc.model;

import com.github.poldroc.ioc.constant.enums.BeanSourceTypeEnum;

import java.util.List;

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

    /**
     * 获取初始化方法
     * @return 初始化方法
     */
    String getInitialize();

    /**
     * 设置初始化方法
     * @param initialize 初始化方法
     */
    void setInitialize(final String initialize);

    /**
     * 获取销毁方法
     * @return 销毁方法
     */
    String getDestroy();

    /**
     * 设置销毁方法
     * @param destroy 销毁方法
     */
    void setDestroy(final String destroy);

    /**
     * 工厂类方法
     * @Param factoryMethod 工厂类方法
     */
    void setFactoryMethod(final String factoryMethod);

    /**
     * 获取工厂类方法
     * @return 工厂类方法
     */
    String getFactoryMethod();

    /**
     * 构造器参数列表
     * @return 构造器参数列表
     */
    List<ConstructorArgDefinition> getConstructorArgList();

    /**
     * 设置构造器参数定义列表
     * @param constructorArgList 构造器参数列表
     */
    void setConstructorArgList(final List<ConstructorArgDefinition> constructorArgList);

    /**
     * 属性参数列表
     * @return 属性参数列表
     */
    List<PropertyArgDefinition> getPropertyArgList();

    /**
     * 设置属性参数定义列表
     * @param propertyArgList 属性参数列表
     */
    void setPropertyArgList(final List<PropertyArgDefinition> propertyArgList);

    /**
     * 是否为抽象类
     * @return 是否
     */
    boolean isAbstractClass();

    /**
     * 设置是否为抽象类
     * @param abstractClass 是否
     */
    void setAbstractClass(final boolean abstractClass);

    /**
     * 获取父类名称
     * @return 父类名称
     */
    String getParentName();

    /**
     * 设置父类名称
     * @param parentName 父类名称
     */
    void setParentName(final String parentName);

    /**
     * 设置对象数据来源
     * @param beanSourceType 对象数据来源
     */
    void setBeanSourceType(final BeanSourceTypeEnum beanSourceType);

    /**
     * 获取对象数据来源
     * @return 数据来源
     */
    BeanSourceTypeEnum getBeanSourceType();
}
