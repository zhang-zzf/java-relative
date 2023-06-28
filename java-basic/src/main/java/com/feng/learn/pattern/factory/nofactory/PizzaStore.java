package com.feng.learn.pattern.factory.nofactory;

import com.feng.learn.pattern.factory.NullPizza;
import com.feng.learn.pattern.factory.Pizza;
import com.feng.learn.pattern.factory.simplefactory.CheesePizza;
import com.feng.learn.pattern.factory.simplefactory.GreekPizza;
import com.feng.learn.pattern.factory.simplefactory.PepperoniPizza;

public class PizzaStore {

  public Pizza orderPizza(String type) {
    Pizza p = new NullPizza();
    if ("cheese".equals(type)) {
      p = new CheesePizza();
    } else if ("greek".equals(type)) {
      p = new GreekPizza();
    } else if ("pepperoni".equals(type)) {
      p = new PepperoniPizza();
    }
    // 你可以想象，每次变化（添加一种pizza），都要修改这里的代码

    p.prepare();
    p.bake();
    p.cut();
    p.box();
    return p;
  }
}
