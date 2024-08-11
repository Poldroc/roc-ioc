package com.github.poldroc.ioc.support.annotation;

import com.github.poldroc.ioc.annotation.Lazy;
import com.github.poldroc.ioc.util.ArgUtil;

import java.lang.reflect.Method;

/**
 * 延迟加载工具类
 *
 * @author Poldroc
 * @date 2024/8/11
 */

public final class Lazys {

    private Lazys() {
    }

    /**
     * 获取指定类的 Lazy 信息
     * 后期可以支持自定义注解，包含元注解 {@link Lazy} 即可。
     *
     * @param clazz 类型
     * @return Lazy 信息
     */
    public static boolean getLazy(final Class clazz) {
        ArgUtil.notNull(clazz, "clazz");

        if (clazz.isAnnotationPresent(Lazy.class)) {
            Lazy lazy = (Lazy) clazz.getAnnotation(Lazy.class);
            return lazy.value();
        }
        return false;
    }

    /**
     * 获取指定方法的 scope 信息
     * 后期可以支持自定义注解，包含元注解 {@link Lazy} 即可。
     *
     * @param method 方法信息
     * @return scope 信息
     */
    public static boolean getLazy(final Method method) {
        ArgUtil.notNull(method, "method");

        if (method.isAnnotationPresent(Lazy.class)) {
            Lazy lazy = (Lazy) method.getAnnotation(Lazy.class);
            return lazy.value();
        }
        return false;
    }

}
