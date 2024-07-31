package com.github.poldroc.ioc.util;

import com.github.poldroc.ioc.exception.IocRuntimeException;

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
}
