package com.feng.learn.pattern.decorator.beverage;

public class HouseBlend extends Beverage {

    public HouseBlend() {
        super("house blend");
    }

    @Override
    public double cost() {
        return 1.2;
    }
}
