package com.feng.learn.pattern.factory;

import java.util.List;

public abstract class Pizza {

  String name;
  String dough;
  String sauce;
  List<String> toppings;

  public void prepare() {

  }

  public void bake() {
    System.out.println("Bake for 25 minutes at 350");
  }

  public void cut() {
    System.out.println("Cutting the pizza into diagonal slices.");
  }

  public void box() {
    System.out.println("Place pizza into official PizzaStore box.");
  }

}
