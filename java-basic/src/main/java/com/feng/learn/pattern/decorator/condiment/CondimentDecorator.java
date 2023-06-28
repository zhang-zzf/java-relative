package com.feng.learn.pattern.decorator.condiment;


import com.feng.learn.pattern.decorator.beverage.Beverage;

public abstract class CondimentDecorator extends Beverage {

  protected Beverage beverage;

  protected CondimentDecorator(Beverage beverage) {
    super("condiment");
    this.beverage = beverage;
  }

  @Override
  public abstract String getDescription();

  @Override
  public abstract double cost();

}
