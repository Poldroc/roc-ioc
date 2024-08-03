package com.github.poldroc.ioc.support.lifecycle;

/**
 * 对象初始化之后调用
 * <p>
 * Initialization methods are called in the same order:
 * <p>
 * 1. Methods annotated with @PostConstruct
 * <p>
 * 2. initialize() as defined by the InitializingBean callback interface
 * <p>
 * 3. A custom configured init() method
 *
 * @author Poldroc
 * @date 2024/8/3
 */

public interface InitializingBean {

    /**
     * 对象初始化完成之后调用
     */
    void initialize();
}
