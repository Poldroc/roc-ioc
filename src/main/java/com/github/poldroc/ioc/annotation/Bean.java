package com.github.poldroc.ioc.annotation;

import java.lang.annotation.*;
/**
 * 用于指定 Bean 信息
 * @author Poldroc
 * @date 2024/8/10
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Bean {

    /**
     * 组件名称
     * @return 组件名称
     */
    String value() default "";

    /**
     * 初始化方法
     * @return 初始化方法
     */
    String initMethod() default "";

    /**
     * 销毁方法
     * @return 销毁方法
     */
    String destroyMethod() default "";

}
