package com.store.pricing;

import com.store.order.Order;

public class StandardShipping implements ShippingRule {
    private double cost;

    public StandardShipping(double cost) {
        this.cost = cost;
    }

    public double calculateShippingCost(Order order) {
        return cost;
    }
}