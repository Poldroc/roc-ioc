package com.github.poldroc.ioc.test.config;

import com.github.poldroc.ioc.annotation.Bean;
import com.github.poldroc.ioc.annotation.Configuration;
import com.github.poldroc.ioc.test.service.WeightApple;

@Configuration
public class AppBeanConfig {

    @Bean
    public WeightApple weightApple() {
        return new WeightApple();
    }

}
