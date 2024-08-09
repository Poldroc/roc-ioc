package com.github.poldroc.ioc.core.impl;

import com.github.poldroc.ioc.constant.enums.ScopeEnum;
import com.github.poldroc.ioc.core.BeanFactory;
import com.github.poldroc.ioc.exception.IocRuntimeException;
import com.github.poldroc.ioc.model.BeanDefinition;
import com.github.poldroc.ioc.model.ConstructorArgDefinition;
import com.github.poldroc.ioc.support.aware.BeanCreateAware;
import com.github.poldroc.ioc.support.aware.BeanNameAware;
import com.github.poldroc.ioc.support.cycle.DependsCheckService;
import com.github.poldroc.ioc.support.cycle.impl.DefaultDependsCheckService;
import com.github.poldroc.ioc.support.lifecycle.DisposableBean;
import com.github.poldroc.ioc.support.lifecycle.InitializingBean;
import com.github.poldroc.ioc.support.lifecycle.create.DefaultNewInstanceBean;
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
     * 依赖检查服务
     */
    private DependsCheckService dependsCheckService = new DefaultDependsCheckService();

    /**
     * 注册对象定义信息
     */
    protected void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        ArgUtil.notEmpty(beanName, "beanName");
        ArgUtil.notNull(beanDefinition, "beanDefinition");
        this.beanDefinitionMap.put(beanName, beanDefinition);

        // 注册类型信息
        this.registerTypeBeanNames(beanName, beanDefinition);

        // 添加监听器
        this.notifyAllBeanNameAware(beanName);

        // 初始化 bean
        if (needEagerCreateSingleton(beanDefinition)) {
            this.registerSingletonBean(beanName, beanDefinition);
        }
    }

    /**
     * 获取依赖检测服务
     * @return 服务实现
     */
    protected DependsCheckService getDependsCheckService() {
        return this.dependsCheckService;
    }

    /**
     * 通知所有的 BeanNameAware
     *
     * @param beanName bean 名称
     */
    private void notifyAllBeanNameAware(String beanName) {
        this.getBeans(BeanNameAware.class).forEach(aw -> aw.setBeanName(beanName));
    }

    /**
     * 通知所有的 BeanCreateAware
     *
     * @param name     bean 名称
     * @param instance 实例
     */
    private void notifyAllBeanCreateAware(String name, Object instance) {
        this.getBeans(BeanCreateAware.class).forEach(aw -> aw.setBeanCreate(name, instance));
    }


    /**
     * 是否需要新建单例
     * （1）如果不需要立刻加载，则进行延迟处理
     * （2）如果存在构造器创建，则判断是否存在 dependsOn
     * 如果 {@link #beanMap} 中包含所有依赖对象，则直接创建，否则需要等待。
     *
     * @param beanDefinition 定义信息
     * @return 是否
     */
    private boolean needEagerCreateSingleton(BeanDefinition beanDefinition) {
        ArgUtil.notNull(beanDefinition, "beanDefinition");

        if (beanDefinition.isLazyInit()) {
            return false;
        }
        List<ConstructorArgDefinition> argDefinitionList = beanDefinition.getConstructorArgList();
        if (argDefinitionList != null && !argDefinitionList.isEmpty()) {
            // 判断是否存在依赖
            for (ConstructorArgDefinition argDefinition : argDefinitionList) {
                String ref = argDefinition.getRef();
                if (!ref.isEmpty()) {
                    Object instance = this.beanMap.get(ref);
                    // 如果不存在依赖对象，则等待
                    if (instance == null) {
                        return false;
                    }
                }
            }
        }

        return true;
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
        Set<Class> typeSet = getTypeSet(beanDefinition);
        for (Class type : typeSet) {
            Set<String> beanNames = typeBeanNameMap.get(type);
            if (beanNames == null) {
                beanNames = new HashSet<>();

            }
            beanNames.add(beanName);
            typeBeanNameMap.put(type, beanNames);
        }
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


    /**
     * 获取 beans 消息列表
     *
     * @param requiredType 指定类型
     * @param <T>          泛型
     * @return 结果列表
     */
    protected <T> List<T> getBeans(Class<T> requiredType) {
        ArgUtil.notNull(requiredType, "requiredType");

        Set<String> beanNames = this.getBeanNames(requiredType);
        if (beanNames == null || beanNames.isEmpty()) {
            return new ArrayList<>();
        }

        List<T> beans = new ArrayList<>();
        for (String name : beanNames) {
            beans.add(this.getBean(name, requiredType));
        }
        return beans;
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
        // 1. 初始化相关处理
        String beanName = beanDefinition.getName();
        // 1.1 检测是否存在循环依赖
        if (dependsCheckService.isCircleRef(beanName)){
            throw new IocRuntimeException(beanName + " has circle reference.");
        }
        // 1.2 创建实例
        Object instance = DefaultNewInstanceBean.getInstance().newInstance(this, beanDefinition);

        // 2. 初始化完成之后的调用
        InitializingBean initializingBean = new DefaultPostConstructBean(instance, beanDefinition);
        initializingBean.initialize();
        // 3. 添加到实例列表中，便于后期销毁
        instanceBeanDefinitionList.add(new Pair<>(instance, beanDefinition));
        // 4. 通知所有的 BeanCreateAware
        this.notifyAllBeanCreateAware(beanName, instance);
        return instance;
    }

    /**
     * 获取类型信息
     * （1）当前类信息
     * （2）所有的接口类信息
     *
     * @param beanDefinition 对象属性
     * @return 对应的 bean 信息
     */
    private Set<Class> getTypeSet(final BeanDefinition beanDefinition) {
        String className = beanDefinition.getClassName();
        Set<Class> classSet = new HashSet<>();
        Class currentClass = ClassUtil.getClass(className);
        classSet.add(currentClass);
        Class[] interfaces = currentClass.getInterfaces();
        if (interfaces != null && interfaces.length > 0) {
            classSet.addAll(Arrays.asList(interfaces));
        }
        return classSet;
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
