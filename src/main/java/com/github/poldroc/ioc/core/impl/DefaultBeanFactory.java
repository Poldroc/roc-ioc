package com.github.poldroc.ioc.core.impl;


import com.github.poldroc.ioc.core.BeanFactory;
import com.github.poldroc.ioc.exception.IocRuntimeException;
import com.github.poldroc.ioc.model.BeanDefinition;
import com.github.poldroc.ioc.util.ArgUtil;
import com.github.poldroc.ioc.util.ClassUtil;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanFactory implements BeanFactory {

    /**
     * 对象信息 map
     */
    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    /**
     * 对象 map
     */
    private Map<String, Object> beanMap = new ConcurrentHashMap<>();


    /**
     * 类型集合
     * （1）主要是为了 type 类型，获取对应的信息为做准备
     * （2）考虑到懒加载的处理。
     *
     * @see #getBean(String, Class) 获取对应对象信息
     */
    private Map<Class, Set<String>> typeBeanNameMap = new ConcurrentHashMap<>();


    /**
     * 注册对象定义信息
     */
    protected void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        // 这里可以添加监听器
        this.beanDefinitionMap.put(beanName, beanDefinition);
        Class type = getType(beanDefinition);
        Set<String> beanNameSet = typeBeanNameMap.get(type);
        if (beanNameSet == null) {
            beanNameSet = new HashSet<>();
        }
        beanNameSet.add(beanName);
        typeBeanNameMap.put(type, beanNameSet);
    }

    /**
     * 根据类型获取对应的属性名称
     *
     * @param requiredType 需求类型
     * @return bean 名称列表
     */
    Set<String> getBeanNames(Class requiredType) {
        ArgUtil.notNull(requiredType, "requiredType");
        return typeBeanNameMap.get(requiredType);
    }

    @Override
    public Object getBean(String beanName) {
        ArgUtil.notNull(beanName, "beanName");
        Object bean = beanMap.get(beanName);
        if (bean != null) {
            // 这里直接返回的是单例，如果用户指定为多例，则每次都需要新建。
            return bean;
        }
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new IocRuntimeException(beanName + " not exists in bean define.");
        }
        Object newBean = createBean(beanDefinition);
        beanMap.put(beanName, newBean);
        return newBean;

    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(String beanName, Class<T> requiredType) {
        ArgUtil.notNull(beanName, "beanName");
        ArgUtil.notNull(requiredType, "requiredType");

        Object object = getBean(beanName);
        return (T) object;
    }

    @Override
    public boolean containsBean(String beanName) {
        ArgUtil.notNull(beanName, "beanName");

        return beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public boolean isTypeMatch(String beanName, Class requiredType) {
        ArgUtil.notNull(beanName, "beanName");
        ArgUtil.notNull(requiredType, "requiredType");

        Class<?> type = getType(beanName);
        return requiredType.equals(type);
    }

    @Override
    public Class<?> getType(String beanName) {
        ArgUtil.notNull(beanName, "beanName");

        Object bean = this.getBean(beanName);
        return bean.getClass();
    }

    private Object createBean(final BeanDefinition beanDefinition) {
        String className = beanDefinition.getClassName();
        Class clazz = ClassUtil.getClass(className);
        return ClassUtil.newInstance(clazz);
    }

    /**
     * 获取类型信息
     * （1）这里主要是一种
     *
     * @param beanDefinition 对象属性
     * @return 对应的 bean 信息
     */
    private Class getType(final BeanDefinition beanDefinition) {
        String className = beanDefinition.getClassName();

        return ClassUtil.getClass(className);
    }


}
