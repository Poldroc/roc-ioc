package com.github.poldroc.ioc.support.aware;


import com.github.poldroc.ioc.context.ApplicationContext;
import com.github.poldroc.ioc.exception.IocRuntimeException;

/**
 * 上下文信息设置监听
 *
 * @author Poldroc
 * @date 2024/8/7
 */

public interface ApplicationContextAware extends Aware {

    /**
     * 设置 applicationContext 的信息
     *
     * @param applicationContext 上下文信息
     * @throws IocRuntimeException 运行时异常
     */
    void setApplicationContext(ApplicationContext applicationContext) throws IocRuntimeException;

}
