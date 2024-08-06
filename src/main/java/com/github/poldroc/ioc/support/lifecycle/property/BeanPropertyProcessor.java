package com.github.poldroc.ioc.support.lifecycle.property;

import com.github.poldroc.ioc.core.BeanFactory;
import com.github.poldroc.ioc.model.PropertyArgDefinition;

import java.util.List;


/**
 * 对象属性设置类。
 * <p>
 * 主要分为两个部分：
 * <p>
 * （1）直接根据属性值设置
 * （2）根据引用属性设置
 *
 * @author Poldroc
 * @date 2024/8/6
 */

public interface BeanPropertyProcessor {

    /**
     * 对象初始化完成之后调用
     *
     * @param beanFactory     属性工厂
     * @param instance        对象实例
     * @param propertyArgList 参数信息列表
     */
    void propertyProcessor(final BeanFactory beanFactory,
                           final Object instance,
                           final List<PropertyArgDefinition> propertyArgList);

}
