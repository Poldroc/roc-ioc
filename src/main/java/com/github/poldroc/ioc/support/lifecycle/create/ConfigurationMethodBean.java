package com.github.poldroc.ioc.support.lifecycle.create;


import com.github.poldroc.ioc.core.BeanFactory;
import com.github.poldroc.ioc.exception.IocRuntimeException;
import com.github.poldroc.ioc.model.AnnotationBeanDefinition;
import com.github.poldroc.ioc.model.BeanDefinition;
import com.github.poldroc.ioc.util.ArgUtil;
import com.github.poldroc.ioc.util.ClassUtil;
import com.github.poldroc.ioc.util.ReflectMethodUtil;

import java.lang.reflect.Method;
import java.util.Optional;

class ConfigurationMethodBean extends AbstractNewInstanceBean {

    /**
     * 单例
     */
    private static final ConfigurationMethodBean INSTANCE = new ConfigurationMethodBean();

    /**
     * 获取单例实例
     *
     * @return 单例
     */
    public static ConfigurationMethodBean getInstance() {
        return INSTANCE;
    }

    @Override
    protected Optional<Object> newInstanceOpt(BeanFactory beanFactory, BeanDefinition beanDefinition, Class beanClass) {
        if (!(beanDefinition instanceof AnnotationBeanDefinition)) {
            throw new IocRuntimeException("beanDefinition must be instance of AnnotationBeanDefinition " + beanDefinition.getName());
        }
        // 直接根据 invoke
        AnnotationBeanDefinition annotationBeanDefinition = (AnnotationBeanDefinition) beanDefinition;
        Object configInstance = beanFactory.getBean(annotationBeanDefinition.getConfigurationName());
        Method method = getMethod(configInstance, annotationBeanDefinition.getConfigurationBeanMethod());

        final Object beanInstance = ReflectMethodUtil.invoke(configInstance, method);
        return Optional.ofNullable(beanInstance);
    }

    /**
     * 获取对应的方法
     *
     * @param configInstance 配置实例
     * @param methodName     方法名称
     * @return 方法实例
     */
    private Method getMethod(final Object configInstance,
                             final String methodName) {
        ArgUtil.notNull(configInstance, "configInstance");
        ArgUtil.notEmpty(methodName, "methodName");

        final Class clazz = configInstance.getClass();
        return ClassUtil.getMethod(clazz, methodName);
    }
}
