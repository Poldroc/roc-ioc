package com.github.poldroc.ioc.context;

import com.github.poldroc.ioc.constant.enums.ScopeEnum;
import com.github.poldroc.ioc.core.impl.DefaultListableBeanFactory;
import com.github.poldroc.ioc.model.BeanDefinition;

import java.util.List;
/**
 * 抽象应用上下文
 * @author Poldroc
 * @date 2024/8/3
 */

public abstract class AbstractApplicationContext extends DefaultListableBeanFactory implements ApplicationContext {
    @Override
    public String getApplicationName() {
        return "application context";
    }

    protected void init() {
        List<? extends BeanDefinition> beanDefinitions = buildBeanDefinitionList();

        this.registerBeanDefinitions(beanDefinitions);

        this.registerShutdownHook();
    }

    /**
     * 抽象方法，由子类实现，用于构建 BeanDefinition 列表
     *
     * @return 属性定义列表
     */
    protected abstract List<? extends BeanDefinition> buildBeanDefinitionList();


    /**
     * 注册对象属性列表
     *
     * @param beanDefinitions 对象属性列表
     */
    private void registerBeanDefinitions(List<? extends BeanDefinition> beanDefinitions) {
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
