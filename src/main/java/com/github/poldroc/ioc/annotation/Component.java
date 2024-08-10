package com.github.poldroc.ioc.annotation;

import java.lang.annotation.*;

/**
 * 用于指定 ioc 管理类属性
 *
 * @author Poldroc
 * @date 2024/8/10
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface Component {

    /**
     * 组件名称
     *
     * @return 组件名称
     */
    String value() default "";

}
