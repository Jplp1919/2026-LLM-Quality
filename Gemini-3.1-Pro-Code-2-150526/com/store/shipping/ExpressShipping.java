package com.store.shipping;

import com.store.exception.ValidationException;

public class ExpressShipping implements ShippingRule {
    private double cost;

    public ExpressShipping(double cost) {
        if (cost < 0) {
            throw new ValidationException("Shipping cost cannot be negative");
        }
        this.cost = cost;
    }

    public double getCost() {
        return cost;
    }
}