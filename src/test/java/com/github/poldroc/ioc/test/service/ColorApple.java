package com.github.poldroc.ioc.test.service;

import com.github.poldroc.ioc.constant.annotation.FactoryMethod;

public class ColorApple {

    /**
     * 颜色
     */
    private String color;

    /**
     * 自定义实例
     * @return 自定义结果
     */
    @FactoryMethod
    public static ColorApple newInstance() {
        ColorApple colorApple = new ColorApple();
        colorApple.setColor("define");
        return colorApple;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "ColorApple{" +
                "color='" + color + '\'' +
                '}';
    }

}
