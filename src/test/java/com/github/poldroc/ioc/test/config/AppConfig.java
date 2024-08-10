package com.github.poldroc.ioc.test.config;

import com.github.poldroc.ioc.annotation.Configuration;

@Configuration
public class AppConfig {

    public String name() {
        return "app config";
    }

}
