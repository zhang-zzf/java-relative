package com.feng.learn.pattern.decorator.condiment;


import com.feng.learn.pattern.decorator.beverage.Beverage;

public class Whip extends CondimentDecorator {

  public Whip(Beverage beverage) {
    super(beverage);
  }

  @Override
  public String getDescription() {
    return beverage.getDescription() + ",whip";
  }

  @Override
  public double cost() {
    return beverage.cost() + .55;
  }
}
