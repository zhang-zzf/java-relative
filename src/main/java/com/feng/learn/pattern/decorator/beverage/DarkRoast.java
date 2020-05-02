package com.feng.learn.pattern.decorator.beverage;

public class DarkRoast extends Beverage {

    public DarkRoast() {
        super("dark roast");
    }

    @Override
    public double cost() {
        return 2.35;
    }
}
