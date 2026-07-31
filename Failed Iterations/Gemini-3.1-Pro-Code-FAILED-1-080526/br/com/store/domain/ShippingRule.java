package br.com.store.domain;

public class ShippingRule {
private String name;
private double cost;

public ShippingRule(String name, double cost) {
    if (cost < 0) {
        throw new IllegalArgumentException();
    }
    this.name = name;
    this.cost = cost;
}

public String getName() {
    return name;
}

public double getCost() {
    return cost;
}
}