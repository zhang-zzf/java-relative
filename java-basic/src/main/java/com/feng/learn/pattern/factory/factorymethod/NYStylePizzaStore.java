package com.feng.learn.pattern.factory.factorymethod;

import com.feng.learn.pattern.factory.NullPizza;
import com.feng.learn.pattern.factory.Pizza;
import com.feng.learn.pattern.factory.factorymethod.chicagostyle.GreekPizza;
import com.feng.learn.pattern.factory.factorymethod.nystyle.CheesePizza;

/**
 * 纽约风味披萨店
 */
public class NYStylePizzaStore extends PizzaStore {

    @Override
    public Pizza createPizza(String type) {
        Pizza p = new NullPizza();
        if ("cheese".equals(type)) {
            p = new CheesePizza();
        }
        else if ("greek".equals(type)) {
            p = new GreekPizza();
        }
        return p;
    }
}
