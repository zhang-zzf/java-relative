package com.feng.learn.pattern.decorator.condiment;


import com.feng.learn.pattern.decorator.beverage.Beverage;

public class Milk extends CondimentDecorator {

  private double cost;

  public Milk(Beverage beverage) {
    this(beverage, 0.35);
  }

  public Milk(Beverage beverage, double cost) {
    super(beverage);
    this.cost = cost;
  }

  @Override
  public String getDescription() {
    return beverage.getDescription() + ",milk";
  }

  @Override
  public double cost() {
    return beverage.cost() + cost;
  }
}
