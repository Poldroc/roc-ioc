package com.github.poldroc.ioc.support.name;


import com.github.poldroc.ioc.model.BeanDefinition;

/**
 * Bean 命名策略
 *
 * @author Poldroc
 * @date 2024/8/10
 */

public interface BeanNameStrategy {

    /**
     * 生成对象名称
     *
     * @param definition bean 属性定义
     * @return 生成的结果名称
     */
    String generateBeanName(BeanDefinition definition);

}
