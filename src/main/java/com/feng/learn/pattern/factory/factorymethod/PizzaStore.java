package com.feng.learn.pattern.factory.factorymethod;


import com.feng.learn.pattern.factory.Pizza;

public abstract class PizzaStore {


    public Pizza orderPizza(String type) {

        /**
         * PizzaStore 并不知道createPizza方法返回的是哪种具体的Pizza
         * 解耦（decouple)
         */
        Pizza p = createPizza(type);

        p.prepare();
        p.bake();
        p.cut();
        p.box();

        return p;
    }

    /**
     * 工厂方法 把创建实例的行为延迟到子类中
     */
    public abstract Pizza createPizza(String type);
}

