package com.store.domain.shipping;

import com.store.domain.Order;

public class StandardShipping implements ShippingRule {
private double cost;

public StandardShipping(double cost) {
    this.cost = cost;
}

public double calculateShipping(Order order) {
    return cost;
}
}