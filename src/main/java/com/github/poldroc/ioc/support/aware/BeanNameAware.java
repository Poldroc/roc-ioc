package com.github.poldroc.ioc.support.aware;


import com.github.poldroc.ioc.exception.IocRuntimeException;

/**
 * bean 名称设置监听
 *
 * @author Poldroc
 * @date 2024/8/7
 */

public interface BeanNameAware extends Aware {

    /**
     * 设置 bean 的名称
     *
     * @param name bean 名称
     * @throws IocRuntimeException 运行时异常
     */
    void setBeanName(String name) throws IocRuntimeException;

}
