package com.github.poldroc.ioc.context;

import com.github.poldroc.ioc.core.ListableBeanFactory;
/**
 * 应用上下文接口
 * @author Poldroc
 * @date 2024/8/2
 */

public interface ApplicationContext extends ListableBeanFactory {

    /**
     * 获取应用名称
     * @return 应用名称
     */
    String getApplicationName();

}
