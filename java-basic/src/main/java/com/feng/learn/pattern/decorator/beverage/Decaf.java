package com.feng.learn.pattern.decorator.beverage;

public class Decaf extends Beverage {

  public Decaf() {
    super("decaf");
  }

  @Override
  public double cost() {
    return 3.55;
  }
}
