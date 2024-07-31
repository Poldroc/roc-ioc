package com.github.poldroc.ioc.core.impl;

import com.github.poldroc.ioc.core.BeanFactory;
import com.github.poldroc.ioc.exception.IocRuntimeException;
import com.github.poldroc.ioc.model.BeanDefinition;
import com.github.poldroc.ioc.util.ClassUtil;

import java.util.Map;
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
     * 注册对象定义信息
     */
    protected void registerBeanDefinition(final String beanName, final BeanDefinition beanDefinition) {
        // 这里可以添加监听器
        this.beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    public Object getBean(String beanName) {
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

    private Object createBean(final BeanDefinition beanDefinition) {
        String className = beanDefinition.getClassName();
        Class clazz = ClassUtil.getClass(className);
        return ClassUtil.newInstance(clazz);
    }


    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(String beanName, Class<T> requiredType) {
        Object object = getBean(beanName);
        return (T)object;
    }

}
