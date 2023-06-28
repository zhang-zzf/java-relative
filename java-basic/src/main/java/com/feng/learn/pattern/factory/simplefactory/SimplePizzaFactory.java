package com.feng.learn.pattern.factory.simplefactory;


import com.feng.learn.pattern.factory.NullPizza;
import com.feng.learn.pattern.factory.Pizza;

public class SimplePizzaFactory {

  public Pizza create(String type) {
    Pizza p = new NullPizza();
    if ("cheese".equals(type)) {
      p = new CheesePizza();
    } else if ("greek".equals(type)) {
      p = new GreekPizza();
    } else if ("pepperoni".equals(type)) {
      p = new PepperoniPizza();
    }

    return p;
  }
}
