package com.github.poldroc.ioc.support.lifecycle.init;

import com.github.houbb.heaven.util.common.ArgUtil;

import com.github.poldroc.ioc.exception.IocRuntimeException;
import com.github.poldroc.ioc.model.BeanDefinition;
import com.github.poldroc.ioc.support.lifecycle.InitializingBean;
import com.github.poldroc.ioc.util.ClassUtil;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * 默认创建对象初始化
 * <p>
 * 1.注解{@link javax.annotation.PostConstruct}标注的方法
 * 2.实现{@link com.github.poldroc.ioc.support.lifecycle.InitializingBean}接口的初始化方法
 * 3.调用{@link com.github.poldroc.ioc.model.BeanDefinition#getInitialize()}方法
 * <p>
 * 针对注解（1）的处理，需要 class 信息结合 {@link com.github.poldroc.ioc.model.BeanDefinition} 进行注解相关信息的拓展。
 *
 * @author Poldroc
 * @date 2024/8/3
 */

public class DefaultPostConstructBean implements InitializingBean {

    /**
     * 对象实例
     */
    private final Object instance;

    /**
     * 对象实例类型
     */
    private final Class instanceClass;

    /**
     * 对象属性定义
     */
    private final BeanDefinition beanDefinition;

    public DefaultPostConstructBean(Object instance, BeanDefinition beanDefinition) {
        ArgUtil.notNull(instance, "instance");
        ArgUtil.notNull(beanDefinition, "beanDefinition");

        this.instance = instance;
        this.instanceClass = instance.getClass();
        this.beanDefinition = beanDefinition;
    }


    @Override
    public void initialize() {

        postConstruct();

        initializingBean();

        customInit();
    }

    private void postConstruct() {
        Optional<Method> methodOptional = ClassUtil.getMethodOptional(instance.getClass(), PostConstruct.class);
        if (!methodOptional.isPresent()) {
            return;
        }
        // 不能有参数
        Method method = methodOptional.get();
        if (method.getParameterCount() != 0) {
            throw new IllegalArgumentException("Method annotated with @PostConstruct must not have any parameters");
        }
        try {
            method.invoke(instance);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IocRuntimeException(e);
        }
    }

    private void initializingBean() {
        if (instance instanceof InitializingBean) {
            ((InitializingBean) instance).initialize();
        }
    }

    private void customInit() {
        String initialize = beanDefinition.getInitialize();
        if (initialize != null && !initialize.isEmpty()) {
            try {
                Method method = instance.getClass().getMethod
                        (initialize);
                method.invoke(instance);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new IocRuntimeException(e);
            }
        }
    }
}
