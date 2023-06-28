package com.feng.learn.pattern.decorator.condiment;


import com.feng.learn.pattern.decorator.beverage.Beverage;

public class Mocha extends CondimentDecorator {

  private double cost;

  public Mocha(Beverage beverage) {
    this(beverage, 0.2);
  }

  public Mocha(Beverage beverage, double cost) {
    super(beverage);
    this.cost = cost;
  }

  @Override
  public String getDescription() {
    return beverage.getDescription() + ",mocha";
  }

  @Override
  public double cost() {
    return beverage.cost() + cost;
  }
}
