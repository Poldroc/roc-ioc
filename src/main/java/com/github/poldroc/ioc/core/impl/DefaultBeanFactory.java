package com.github.poldroc.ioc.core.impl;


import com.github.poldroc.ioc.constant.enums.ScopeEnum;
import com.github.poldroc.ioc.core.BeanFactory;
import com.github.poldroc.ioc.exception.IocRuntimeException;
import com.github.poldroc.ioc.model.BeanDefinition;
import com.github.poldroc.ioc.support.lifecycle.DisposableBean;
import com.github.poldroc.ioc.support.lifecycle.InitializingBean;
import com.github.poldroc.ioc.support.lifecycle.destroy.DefaultPreDestroyBean;
import com.github.poldroc.ioc.support.lifecycle.init.DefaultPostConstructBean;
import com.github.poldroc.ioc.util.ArgUtil;
import com.github.poldroc.ioc.util.ClassUtil;
import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanFactory implements BeanFactory, DisposableBean {

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
     * 实例 与 bean 定义信息 map
     */
    private List<Pair<Object, BeanDefinition>> instanceBeanDefinitionList = new ArrayList<>();

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
     *
     * @param beanName       bean 名称
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
        // 这里需要考虑 scope 的处理
        String scope = beanDefinition.getScope();
        // 如果是多例，直接创建新的对象即可
        if (!ScopeEnum.SINGLETON.getCode().equals(scope)) {
            return this.createBean(beanDefinition);
        }
        // 单例的处理
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

    /**
     * 根据对象定义信息创建对象
     * （1）注解 {@link javax.annotation.PostConstruct}
     * （2）添加 {@link com.github.poldroc.ioc.support.lifecycle.InitializingBean} 初始化相关处理
     * （3）添加 {@link BeanDefinition#getInitialize()} 初始化相关处理
     * <p>
     * TODO:
     * 1.后期添加关于构造器信息的初始化
     * 2.添加对应的BeanPostProcessor处理
     *
     * @param beanDefinition 对象属性
     * @return 对象实例
     */
    private Object createBean(final BeanDefinition beanDefinition) {
        String className = beanDefinition.getClassName();
        Class clazz = ClassUtil.getClass(className);
        Object instance = ClassUtil.newInstance(clazz);

        // 1. 初始化相关处理
        // 1.1 直接根据构造器
        // 1.2 根据构造器，属性，静态方法
        // 1.3 根据注解处理相关信息

        // 2. 初始化完成之后的调用
        InitializingBean initializingBean = new DefaultPostConstructBean(instance, beanDefinition);
        initializingBean.initialize();
        // 3. 添加到实例列表中，便于后期销毁
        instanceBeanDefinitionList.add(new Pair<>(instance, beanDefinition));
        return instance;
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


    @Override
    public void destroy() {
        for (Pair<Object, BeanDefinition> pair : instanceBeanDefinitionList) {
            Object instance = pair.getKey();
            BeanDefinition beanDefinition = pair.getValue();
            DisposableBean disposableBean = new DefaultPreDestroyBean(instance, beanDefinition);
            disposableBean.destroy();
        }
    }
}
