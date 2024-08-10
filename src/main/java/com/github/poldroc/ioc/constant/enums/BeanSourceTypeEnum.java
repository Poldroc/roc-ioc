package com.github.poldroc.ioc.constant.enums;

/**
 * 对象资源类型枚举
 *
 * @author Poldroc
 * @date 2024/8/10
 */

public enum BeanSourceTypeEnum {

    /**
     * 通过资源文件配置
     */
    RESOURCE,

    /**
     * 配置注解类
     */
    CONFIGURATION,

    /**
     * 配置注解 bean 类
     */
    CONFIGURATION_BEAN;

}
