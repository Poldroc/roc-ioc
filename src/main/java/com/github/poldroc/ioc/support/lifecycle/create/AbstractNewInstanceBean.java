package com.github.poldroc.ioc.support.lifecycle.create;

import com.github.poldroc.ioc.core.BeanFactory;
import com.github.poldroc.ioc.model.BeanDefinition;
import com.github.poldroc.ioc.support.lifecycle.NewInstanceBean;
import com.github.poldroc.ioc.util.ArgUtil;
import com.github.poldroc.ioc.util.ClassUtil;

import java.util.Optional;

/**
 * 抽象对象实例的实现
 * @author Poldroc
 * @date 2024/8/3
 */

public abstract class AbstractNewInstanceBean implements NewInstanceBean {

    /**
     * 创建新对象实例
     * @param beanFactory 对象工厂
     * @param beanClass 类信息
     * @param beanDefinition 对象定义信息
     * @return 实例 optional 信息
     */
    protected abstract Optional<Object> newInstanceOpt(final BeanFactory beanFactory,
                                                       final BeanDefinition beanDefinition,
                                                       final Class beanClass);



    @Override
    public Object newInstance(BeanFactory beanFactory, BeanDefinition beanDefinition) {
        ArgUtil.notNull(beanFactory, "beanFactory");
        ArgUtil.notNull(beanDefinition, "beanDefinition");
        Class beanClass = ClassUtil.getClass(beanDefinition.getClassName());
        Optional<Object> optional = newInstanceOpt(beanFactory, beanDefinition, beanClass);
        return optional.orElse(null);
    }
}
