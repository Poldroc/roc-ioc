package com.github.poldroc.ioc.annotation;

import java.lang.annotation.*;

/**
 * 用于指定 java 配置注解
 *
 * @author Poldroc
 * @date 2024/8/10
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface Configuration {

    /**
     * 组件名称
     *
     * @return 组件名称
     */
    String value() default "";

}
