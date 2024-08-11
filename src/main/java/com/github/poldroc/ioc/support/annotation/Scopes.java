package com.github.poldroc.ioc.support.annotation;

import com.github.poldroc.ioc.annotation.Scope;
import com.github.poldroc.ioc.constant.ScopeConst;
import com.github.poldroc.ioc.util.ArgUtil;

import java.lang.reflect.Method;

/**
 * 延迟加载工具类
 *
 * @author Poldroc
 * @date 2024/8/11
 */

public final class Scopes {

    private Scopes() {
    }

    /**
     * 获取指定类的 scope 信息
     * 后期可以支持自定义注解，包含元注解 {@link Scope} 即可。
     *
     * @param clazz 类型
     * @return scope 信息
     */
    public static String getScope(final Class clazz) {
        ArgUtil.notNull(clazz, "clazz");

        if (clazz.isAnnotationPresent(Scope.class)) {
            Scope scope = (Scope) clazz.getAnnotation(Scope.class);
            return scope.value();
        }
        return ScopeConst.SINGLETON;
    }

    /**
     * 获取指定方法的 scope 信息
     * 后期可以支持自定义注解，包含元注解 {@link Scope} 即可。
     *
     * @param method 方法信息
     * @return scope 信息
     */
    public static String getScope(final Method method) {
        ArgUtil.notNull(method, "method");

        if (method.isAnnotationPresent(Scope.class)) {
            Scope scope = (Scope) method.getAnnotation(Scope.class);
            return scope.value();
        }
        return ScopeConst.SINGLETON;
    }

}
