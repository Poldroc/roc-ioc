package com.github.poldroc.ioc.support.lifecycle.destroy;

import com.github.houbb.heaven.util.common.ArgUtil;

import com.github.poldroc.ioc.exception.IocRuntimeException;
import com.github.poldroc.ioc.model.BeanDefinition;
import com.github.poldroc.ioc.support.lifecycle.DisposableBean;
import com.github.poldroc.ioc.util.ClassUtil;

import javax.annotation.PreDestroy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * 默认销毁对象
 * <p>
 * 1.注解{@link javax.annotation.PreDestroy}标注的方法
 * 2.实现{@link com.github.poldroc.ioc.support.lifecycle.DisposableBean}接口的销毁方法
 * 3.调用{@link com.github.poldroc.ioc.model.BeanDefinition#getDestroy()}方法
 * <p>
 * 针对注解（1）的处理，需要 class 信息结合 {@link com.github.poldroc.ioc.model.BeanDefinition} 进行注解相关信息的拓展。
 *
 * @author Poldroc
 * @date 2024/8/3
 */

public class DefaultPreDestroyBean implements DisposableBean {

    /**
     * 对象实例
     */
    private final Object instance;

    /**
     * 对象属性定义
     */
    private final BeanDefinition beanDefinition;

    /**
     * 实例类型信息
     */
    private final Class instanceClass;

    public DefaultPreDestroyBean(Object instance, BeanDefinition beanDefinition) {
        ArgUtil.notNull(instance, "instance");
        ArgUtil.notNull(beanDefinition, "beanDefinition");


        this.instance = instance;
        this.beanDefinition = beanDefinition;
        this.instanceClass = instance.getClass();
    }

    @Override
    public void destroy() {
        preDestroy();

        disposableBean();

        customDestroy();

    }

    private void preDestroy() {
        Optional<Method> methodOptional = ClassUtil.getMethodOptional(instance.getClass(), PreDestroy.class);
        if (!methodOptional.isPresent()) {
            return;
        }
        // 不能有参数
        Method method = methodOptional.get();
        if (method.getParameterCount() != 0) {
            throw new IocRuntimeException("Method annotated with @PreDestroy must not have any parameters.");
        }
        try {
            method.invoke(instance);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IocRuntimeException(e);
        }
    }

    private void disposableBean() {
        if (instance instanceof DisposableBean) {
            ((DisposableBean) instance).destroy();
        }
    }

    private void customDestroy() {
        String destroy = beanDefinition.getDestroy();
        if (destroy != null && !destroy.isEmpty()) {
            try {
                Method method = instance.getClass().getMethod
                        (destroy);
                method.invoke(instance);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new IocRuntimeException(e);
            }
        }
    }

}
