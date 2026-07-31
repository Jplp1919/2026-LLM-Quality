package com.store.shipping;

import com.store.exception.ValidationException;

public class StandardShipping implements ShippingRule {
    private double cost;

    public StandardShipping(double cost) {
        if (cost < 0) {
            throw new ValidationException("Shipping cost cannot be negative");
        }
        this.cost = cost;
    }

    public double getCost() {
        return cost;
    }
}