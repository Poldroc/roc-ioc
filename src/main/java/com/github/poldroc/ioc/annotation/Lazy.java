package com.github.poldroc.ioc.annotation;

import java.lang.annotation.*;
/**
 * 是否延迟加载
 * @author Poldroc
 * @date 2024/8/11
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Lazy {

    /**
     * 是否延迟加载
     * @return 是否延迟加载
     */
    boolean value() default false;

}
