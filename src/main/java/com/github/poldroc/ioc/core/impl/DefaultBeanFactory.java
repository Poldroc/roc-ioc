package com.github.poldroc.ioc.core.impl;


import com.github.poldroc.ioc.constant.enums.ScopeEnum;
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
        ArgUtil.notEmpty(beanName, "beanName");
        ArgUtil.notNull(beanDefinition, "beanDefinition");
        // 这里可以添加监听器
        this.beanDefinitionMap.put(beanName, beanDefinition);

        this.registerTypeBeanNames(beanName, beanDefinition);

        boolean isLazyInit = beanDefinition.isLazyInit();
        if (!isLazyInit) {
            this.registerSingletonBean(beanName, beanDefinition);
        }
    }

    /**
     * 注册单例且渴望初期初始化的对象
     * （1）如果是 singleton & lazy-init=false 则进行初始化处理
     * （2）创建完成后，对象放入 {@link #beanMap} 中，便于后期使用
     * （3）
     * @param beanName bean 名称
     * @param beanDefinition 对象定义
     */
    private Object registerSingletonBean(String beanName, BeanDefinition beanDefinition) {
        Object bean = beanMap.get(beanName);

        if (bean != null) {
            return bean;
        }
        Object newBean = createBean(beanDefinition);
        beanMap.put(beanName, newBean);
        return newBean;
    }

    /**
     * 注册类型和 beanNames 信息
     */
    private void registerTypeBeanNames(String beanName, BeanDefinition beanDefinition) {
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
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new IocRuntimeException("beanName: " + beanName + " not exist.");
        }
        String scope = beanDefinition.getScope();
        if(!ScopeEnum.SINGLETON.getCode().equals(scope)){
            return this.createBean(beanDefinition);
        }
        return this.registerSingletonBean(beanName, beanDefinition);
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
