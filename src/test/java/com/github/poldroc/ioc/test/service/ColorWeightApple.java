package com.github.poldroc.ioc.test.service;

public class ColorWeightApple {

    /**
     * 重量
     */
    private Integer weight;

    /**
     * 颜色
     */
    private String color;

    public ColorWeightApple(final ColorApple colorApple, Integer weight) {
        this.color = colorApple.getColor();
        this.weight = weight;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "ColorWeightApple{" +
                "weight=" + weight +
                ", color='" + color + '\'' +
                '}';
    }

}
