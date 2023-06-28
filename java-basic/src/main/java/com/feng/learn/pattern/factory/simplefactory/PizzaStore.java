package com.feng.learn.pattern.factory.simplefactory;


import com.feng.learn.pattern.factory.Pizza;

public class PizzaStore {

  SimplePizzaFactory factory;

  public PizzaStore(SimplePizzaFactory factory) {
    this.factory = factory;
  }

  public Pizza orderPizza(String type) {
    Pizza p = factory.create(type);

    p.prepare();
    p.bake();
    p.cut();
    p.box();
    return p;
  }
}
