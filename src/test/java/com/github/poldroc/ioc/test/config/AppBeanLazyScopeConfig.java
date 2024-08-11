package com.github.poldroc.ioc.test.config;

import com.github.poldroc.ioc.annotation.Bean;
import com.github.poldroc.ioc.annotation.Configuration;
import com.github.poldroc.ioc.annotation.Lazy;
import com.github.poldroc.ioc.annotation.Scope;
import com.github.poldroc.ioc.constant.ScopeConst;
import com.github.poldroc.ioc.test.service.Apple;
import com.github.poldroc.ioc.test.service.WeightApple;

@Configuration
public class AppBeanLazyScopeConfig {

    @Bean
    public WeightApple weightApple() {
        return new WeightApple();
    }

    /**
     * 设置为懒加载多例
     * @return 结果
     */
    @Bean
    @Scope(ScopeConst.PROTOTYPE)
    @Lazy(true)
    public Apple apple() {
        return new Apple();
    }

}
