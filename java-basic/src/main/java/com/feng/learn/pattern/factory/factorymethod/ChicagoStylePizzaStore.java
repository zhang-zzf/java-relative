package com.feng.learn.pattern.factory.factorymethod;

import com.feng.learn.pattern.factory.NullPizza;
import com.feng.learn.pattern.factory.Pizza;
import com.feng.learn.pattern.factory.factorymethod.chicagostyle.CheesePizza;
import com.feng.learn.pattern.factory.factorymethod.nystyle.GreekPizza;

public class ChicagoStylePizzaStore extends PizzaStore {

    @Override
    public Pizza createPizza(String type) {
        Pizza p = new NullPizza();
        if ("cheese".equals(type)) {
            p = new CheesePizza();
        } else if ("greek".equals(type)) {
            p = new GreekPizza();
        }
        return p;
    }
}
