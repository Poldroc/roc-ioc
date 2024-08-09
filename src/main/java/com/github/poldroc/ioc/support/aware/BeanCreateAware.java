package com.github.poldroc.ioc.support.aware;


import com.github.poldroc.ioc.exception.IocRuntimeException;

/**
 * 对象创建监听
 *
 * @author Poldroc
 * @date 2024/8/7
 */

public interface BeanCreateAware extends Aware {

    /**
     * 设值创建的对象
     *
     * @param name bean 名称
     * @throws IocRuntimeException 运行时异常
     */
    void setBeanCreate(String name, final Object instance) throws IocRuntimeException;

}
