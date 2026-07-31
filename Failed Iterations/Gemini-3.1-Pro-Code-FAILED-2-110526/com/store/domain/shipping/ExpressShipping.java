package com.store.domain.shipping;

import com.store.domain.Order;

public class ExpressShipping implements ShippingRule {
private double cost;

public ExpressShipping(double cost) {
    this.cost = cost;
}

public double calculateShipping(Order order) {
    return cost;
}
}