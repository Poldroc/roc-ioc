package com.github.poldroc.ioc.support.cycle;

import com.github.poldroc.ioc.model.BeanDefinition;

import java.util.List;

/**
 * 依赖检查服务
 *
 * @author Poldroc
 * @date 2024/8/9
 */

public interface DependsCheckService {

    /**
     * 注册所有的 bean 定义信息
     *
     * @param beanDefinitionList bean 定义信息
     */
    void registerBeanDefinitions(final List<BeanDefinition> beanDefinitionList);

    /**
     * 是否是循环依赖
     *
     * @param beanName 对象名称
     * @return 是否依赖
     */
    boolean isCircleRef(String beanName);

}
