package com.github.poldroc.ioc.core;

import java.util.List;

/**
 * bean 工厂接口
 *
 * @author Poldroc
 * @date 2024/8/1
 */

public interface ListableBeanFactory extends BeanFactory {

    /**
     * 获取指定类型的实现
     * （1）如果对应类型不存在，则直接抛出异常
     * （2）返回对应的列表信息
     *
     * @param requiredType 类型
     * @param <T>          泛型
     * @return bean 列表
     */
    <T> List<T> getBeans(Class<T> requiredType);

}
