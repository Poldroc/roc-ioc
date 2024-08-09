package com.github.poldroc.ioc.context;


import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.poldroc.ioc.constant.enums.ScopeEnum;
import com.github.poldroc.ioc.core.impl.DefaultListableBeanFactory;
import com.github.poldroc.ioc.exception.IocRuntimeException;
import com.github.poldroc.ioc.model.BeanDefinition;
import com.github.poldroc.ioc.model.PropertyArgDefinition;
import com.github.poldroc.ioc.support.aware.ApplicationContextAware;
import com.github.poldroc.ioc.support.lifecycle.processor.ApplicationContextPostProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 抽象应用上下文
 *
 * @author Poldroc
 * @date 2024/8/3
 */

public abstract class AbstractApplicationContext extends DefaultListableBeanFactory implements ApplicationContext {


    /**
     * 全部的对象定义 map
     */
    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    /**
     * 子对象定义类表
     */
    private List<BeanDefinition> childBeanDefinitionList = new ArrayList<>();

    /**
     * 抽象的对象信息列表
     */
    private List<BeanDefinition> abstractDefinitionList = new ArrayList<>();

    /**
     * 可创建的定义列表信息
     */
    private List<BeanDefinition> createAbleDefinitionList = new ArrayList<>();


    @Override
    public String getApplicationName() {
        return "application context";
    }

    protected void init() {
        // 原始数据信息
        List<BeanDefinition> beanDefinitionList = buildBeanDefinitionList();

        // 这里对 bean 进行统一的处理
        this.buildCreateAbleBeanDefinitionList(beanDefinitionList);

        // 允许用户自定义的部分。
        createAbleDefinitionList = this.postProcessor(createAbleDefinitionList);

        // 注册依赖信息
        super.getDependsCheckService().registerBeanDefinitions(createAbleDefinitionList);

        // 基本属性的设置
        this.registerBeanDefinitions(createAbleDefinitionList);

        this.registerShutdownHook();

        this.notifyAllAware();
    }

    /**
     * 对象定义列表
     * （1）将 abstract 类型的 beanDefine 区分开。
     * （2）对 parent 指定的 beanDefine 进行属性赋值处理。
     *
     * @param beanDefinitionList 所有的对象列表
     */
    void buildCreateAbleBeanDefinitionList(List<BeanDefinition> beanDefinitionList) {
        // 特殊类型首先进行处理
        for (BeanDefinition beanDefinition : beanDefinitionList) {
            String name = beanDefinition.getName();
            String parentName = beanDefinition.getParentName();

            beanDefinitionMap.put(name, beanDefinition);

            if (beanDefinition.isAbstractClass()) {
                abstractDefinitionList.add(beanDefinition);
            } else if (parentName != null && !parentName.isEmpty()) {
                childBeanDefinitionList.add(beanDefinition);
            } else {
                createAbleDefinitionList.add(beanDefinition);
            }
        }

        // 重新统一构建
        this.addAllChildBeanDefinition();
    }

    /**
     * 构建子节点定义列表
     */
    private void addAllChildBeanDefinition() {
        for (BeanDefinition child : childBeanDefinitionList) {
            final String name = child.getName();
            final String parentName = child.getParentName();
            if (name == null || name.isEmpty()) {
                throw new IocRuntimeException("name can not be empty!");
            }
            if (name.equals(parentName)) {
                throw new IocRuntimeException(name + " parent bean is ref to itself!");
            }

            BeanDefinition parentDefinition = beanDefinitionMap.get(parentName);
            if (parentDefinition == null) {
                throw new IocRuntimeException(parentName + " not found !");
            }

            BeanDefinition newChild = buildChildBeanDefinition(child, parentDefinition);
            // 设置所有子类不为 null 的属性。
            newChild.setName(name);

            createAbleDefinitionList.add(newChild);
        }
    }

    /**
     * 构建新的子类属性定义
     * 注意：
     * （1）为了简化，只继承 property 属性信息。
     * （2）父类的属性，子类必须全部都有，否则就会报错，暂时不做优化处理。
     * <p>
     * 核心流程：
     * <p>
     * （1）获取 child 中的所有 property 属性
     * （2）获取 parent 中所有的 property 属性
     * （3）进行过滤处理，相同的以  child 为准。
     *
     * @param child  子类
     * @param parent 父类
     * @return 属性定义
     */
    private BeanDefinition buildChildBeanDefinition(BeanDefinition child, BeanDefinition parent) {
        List<PropertyArgDefinition> childList = child.getPropertyArgList();
        if (childList == null) {
            childList = new ArrayList<>();
        }
        Map<String, PropertyArgDefinition> childArgsMap = new HashMap<>();
        for (PropertyArgDefinition arg : childList) {
            childArgsMap.put(arg.getName(), arg);
        }
        List<PropertyArgDefinition> parentArgs = parent.getPropertyArgList();
        if (parentArgs != null && !parentArgs.isEmpty()) {
            for (PropertyArgDefinition arg : parentArgs) {
                if (!childArgsMap.containsKey(arg.getName())) {
                    childList.add(arg);
                }
            }
        }
        child.setPropertyArgList(childList);
        return child;
    }

    /**
     * 循环执行 bean 信息处理
     *
     * @param beanDefinitions 对象基本信息
     * @return 结果列表
     */
    private List<BeanDefinition> postProcessor(List<BeanDefinition> beanDefinitions) {
        List<ApplicationContextPostProcessor> postProcessors = super.getBeans(ApplicationContextPostProcessor.class);

        for (ApplicationContextPostProcessor processor : postProcessors) {
            beanDefinitions = processor.beforeRegister(beanDefinitions);
        }
        return beanDefinitions;
    }

    /**
     * 注册处理所有的 {@link ApplicationContextAware} 监听器
     */
    private void notifyAllAware() {
        List<ApplicationContextAware> awareList = super.getBeans(ApplicationContextAware.class);

        for (ApplicationContextAware aware : awareList) {
            aware.setApplicationContext(this);
        }
    }

    /**
     * 抽象方法，由子类实现，用于构建 BeanDefinition 列表
     *
     * @return 属性定义列表
     */
    protected abstract List<BeanDefinition> buildBeanDefinitionList();


    /**
     * 注册对象属性列表
     *
     * @param beanDefinitions 对象属性列表
     */
    private void registerBeanDefinitions(List<BeanDefinition> beanDefinitions) {
        if (beanDefinitions != null && !beanDefinitions.isEmpty()) {
            for (BeanDefinition beanDefinition : beanDefinitions) {
                this.fillDefaultValue(beanDefinition);
                super.registerBeanDefinition(beanDefinition.getName(), beanDefinition);
            }
        }
    }

    /**
     * 填充默认值信息
     *
     * @param beanDefinition 对象属性定义
     */
    private void fillDefaultValue(BeanDefinition beanDefinition) {
        // 设置默认作用域为单例
        String scope = beanDefinition.getScope();
        if (scope == null || scope.isEmpty()) {
            beanDefinition.setScope(ScopeEnum.SINGLETON.getCode());
        }
    }

    /**
     * 注册关闭钩子
     */
    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(AbstractApplicationContext.this::destroy));
    }


}
