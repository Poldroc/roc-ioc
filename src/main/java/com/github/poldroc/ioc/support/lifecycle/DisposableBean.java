package com.github.poldroc.ioc.support.lifecycle;

/**
 * 对象被销毁之前调用
 * <p>
 * Destroy methods are called in the same order:
 * <p>
 * 1. Methods annotated with @PreDestroy
 * <p>
 * 2. destroy() as defined by the DisposableBean callback interface
 * <p>
 * 3. A custom configured destroy() method
 *
 * @author Poldroc
 * @date 2024/8/3
 */

public interface DisposableBean {

    void destroy();
}
