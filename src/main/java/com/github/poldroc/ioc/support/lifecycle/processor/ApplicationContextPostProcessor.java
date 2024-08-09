package com.github.poldroc.ioc.support.lifecycle.processor;

import com.github.poldroc.ioc.model.BeanDefinition;

import java.util.List;

public interface ApplicationContextPostProcessor extends PostProcessor {

    /**
     * 对象属性注册之前
     * @param definitionList 对象原始定义信息列表
     * @return 结果
     */
    List<BeanDefinition> beforeRegister(List<BeanDefinition> definitionList);

}
