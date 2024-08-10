package com.github.poldroc.ioc.support.lifecycle.create;


import com.github.poldroc.ioc.annotation.FactoryMethod;
import com.github.poldroc.ioc.core.BeanFactory;
import com.github.poldroc.ioc.model.BeanDefinition;
import com.github.poldroc.ioc.util.ClassUtil;
import com.github.poldroc.ioc.util.ReflectMethodUtil;

import java.lang.reflect.Method;
import java.util.Optional;
/**
 * 根据静态方法创建对象实例
 * @author Poldroc
 * @date 2024/8/3
 */

public class FactoryMethodNewInstanceBean extends AbstractNewInstanceBean {

    private static final FactoryMethodNewInstanceBean INSTANCE = new FactoryMethodNewInstanceBean();

    public static FactoryMethodNewInstanceBean getInstance() {
        return INSTANCE;
    }

    @Override
    protected Optional<Object> newInstanceOpt(BeanFactory beanFactory, BeanDefinition beanDefinition, Class beanClass) {
        // 指定了该方法
        Optional<Object> customOpt = newInstance(beanClass, beanDefinition.getFactoryMethod());
        if (customOpt.isPresent()) {
            return customOpt;
        }

        // 通过注解指定
        return newInstance(beanClass);
    }

    /**
     * 根据指定的方法名称创建对象信息
     *
     * @param beanClass         对象信息
     * @param factoryMethodName 方法名称
     * @return 实例结果
     */
    private Optional<Object> newInstance(final Class beanClass,
                                         final String factoryMethodName) {
        if (factoryMethodName != null) {
            Method method = ClassUtil.getMethod(beanClass, factoryMethodName);
            Object res = ReflectMethodUtil.invokeFactoryMethod(beanClass, method);
            return Optional.ofNullable(res);
        }
        return Optional.empty();
    }

    /**
     * 根据注解创建对象信息
     *
     * @param beanClass 对象信息
     * @return 实例结果
     */
    private Optional<Object> newInstance(final Class beanClass) {
        Optional<Method> methodOptional = ClassUtil.getMethodOptional(beanClass, FactoryMethod.class);
        if (!methodOptional.isPresent()) {
            return Optional.empty();
        }
        Method factoryMethod = methodOptional.get();
        Object res = ReflectMethodUtil.invokeFactoryMethod(beanClass, factoryMethod);
        return Optional.ofNullable(res);
    }

}
