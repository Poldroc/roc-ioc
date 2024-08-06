package com.github.poldroc.ioc.util;


import com.github.houbb.heaven.response.exception.CommonRuntimeException;
import com.github.poldroc.ioc.exception.IocRuntimeException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Optional;

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

    public static Method getMethod(Class clazz, String methodName, Class... paramTypes) {
        com.github.houbb.heaven.util.common.ArgUtil.notNull(clazz, "clazz");
        com.github.houbb.heaven.util.common.ArgUtil.notEmpty(methodName, "methodName");

        try {
            return clazz.getMethod(methodName, paramTypes);
        } catch (NoSuchMethodException var4) {
            NoSuchMethodException e = var4;
            throw new CommonRuntimeException(e);
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
     * 获取构造器
     *
     * @param clazz      类
     * @param paramTypes 参数类型
     * @return 构造器
     */
    public static Constructor getConstructor(Class clazz, Class... paramTypes) {
        ArgUtil.notNull(clazz, "clazz");
        try {
            return clazz.getConstructor(paramTypes);
        } catch (NoSuchMethodException var3) {
            NoSuchMethodException e = var3;
            throw new CommonRuntimeException(e);
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
