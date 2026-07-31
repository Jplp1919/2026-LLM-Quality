package com.store.pricing;

import com.store.order.Order;

public class ExpressShipping implements ShippingRule {
    private double cost;

    public ExpressShipping(double cost) {
        this.cost = cost;
    }

    public double calculateShippingCost(Order order) {
        return cost;
    }
}