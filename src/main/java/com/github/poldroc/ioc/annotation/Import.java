package com.github.poldroc.ioc.annotation;

import java.lang.annotation.*;
/**
 * 用于导入配置信息
 * @author Poldroc
 * @date 2024/8/11
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Import {

    /**
     * 导入配置类信息
     * @return 配置类数组
     */
    Class[] value();

}
