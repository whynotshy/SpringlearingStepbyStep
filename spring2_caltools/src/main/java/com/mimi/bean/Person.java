package com.mimi.bean;

import javax.naming.Name;

/**
 * @programe: testspring
 * @description:
 * @author:JACKY
 * @create: 2021-04-05 10:07
 */
public class Person {
    private String Name;
    private double Height;
    private double Weight;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getHeight() {
        return Height;
    }

    public void setHeight(double height) {
        this.Height = height;
    }

    public double getWeight() {
        return Weight;
    }

    public void setWeight(double weight) {
        this.Weight = weight;
    }
}
