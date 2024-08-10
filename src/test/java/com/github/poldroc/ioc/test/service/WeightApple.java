package com.github.poldroc.ioc.test.service;

public class WeightApple {

    public void init() {
        System.out.println("WeightApple init");
    }

    public void destroy() {
        System.out.println("WeightApple destroy");
    }

    /**
     * 获取重量配置
     * @return 重量
     */
    public String getWeight() {
        return "10";
    }

}
