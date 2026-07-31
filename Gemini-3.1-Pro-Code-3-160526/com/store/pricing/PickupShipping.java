package com.store.pricing;

import com.store.order.Order;

public class PickupShipping implements ShippingRule {
    public double calculateShippingCost(Order order) {
        return 0.0;
    }
}