package com.github.poldroc.ioc.annotation;

import com.github.poldroc.ioc.constant.ScopeConst;

import java.lang.annotation.*;

/**
 * 用于指生命周期 scope
 *
 * @author Poldroc
 * @date 2024/8/11
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Scope {

    /**
     * 生命周期指定
     *
     * @return 生命周期指定
     * @see ScopeConst 常量
     */
    String value() default ScopeConst.SINGLETON;

}
