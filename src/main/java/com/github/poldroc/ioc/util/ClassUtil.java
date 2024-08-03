package com.github.poldroc.ioc.util;

import com.github.houbb.heaven.util.util.Optional;
import com.github.poldroc.ioc.exception.IocRuntimeException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 类工具
 *
 * @author Poldroc
 * @date 2024/7/31
 */
public class ClassUtil {

    /**
     * 获取当前类加载器
     */
    public static ClassLoader currentClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 获取类信息
     */
    public static Class getClass(final String className) {
        ArgUtil.notEmpty(className, "className");

        try {
            return currentClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new IocRuntimeException(e);
        }
    }

    /**
     * 直接根据 class 无参构造器创建实例
     *
     * @param clazz 类信息
     * @return 实例
     */
    public static Object newInstance(final Class clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IocRuntimeException(e);
        }
    }

    /**
     * 获取指定注解的方法
     *
     * @param tClazz     类
     * @param annotation 注解
     * @return 方法的 Optional
     */
    public static Optional<Method> getMethodOptional(final Class tClazz, final Class<? extends Annotation> annotation) {
        Method[] methods = tClazz.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(annotation)) {
                return Optional.of(method);
            }
        }
        return Optional.empty();
    }
}
