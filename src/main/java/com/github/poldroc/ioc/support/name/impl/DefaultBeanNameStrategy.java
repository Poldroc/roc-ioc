package com.github.poldroc.ioc.support.name.impl;


import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.poldroc.ioc.model.BeanDefinition;
import com.github.poldroc.ioc.support.name.BeanNameStrategy;
import com.github.poldroc.ioc.util.ArgUtil;

/**
 * Bean 命名策略
 *
 * @author Poldroc
 * @date 2024/8/10
 */

public class DefaultBeanNameStrategy implements BeanNameStrategy {

    /**
     * 生成对象名称
     * （1）默认直接类名称首字母小写即可。
     * （2）如果已经指定 {@link BeanDefinition#getName()}，则直接返回即可。
     *
     * @param definition bean 属性定义
     * @return 生成的结果名称
     */
    @Override
    public String generateBeanName(BeanDefinition definition) {
        final String name = definition.getName();
        if (StringUtil.isNotEmpty(name)) {
            return name;
        }

        String className = definition.getClassName();
        ArgUtil.notEmpty(className, "className");
        String classSimpleName = className.substring(className.lastIndexOf(".") + 1);
        return StringUtil.firstToLowerCase(classSimpleName);
    }

}
