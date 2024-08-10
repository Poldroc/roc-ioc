package com.github.poldroc.ioc.support.scanner;


import com.github.poldroc.ioc.model.BeanDefinition;

import java.util.Set;

/**
 * Bean 定义扫描器
 * @author Poldroc
 * @date 2024/8/10
 */

public interface BeanDefinitionScanner {

    /**
     * 扫描指定的包，返回对应的 {@link BeanDefinition} 信息集合
     * @param packageNames 包名称数组
     * @return 结果列表
     */
    Set<BeanDefinition> scan(final String ... packageNames);

}
