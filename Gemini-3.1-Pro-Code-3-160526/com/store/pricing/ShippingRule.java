package com.store.pricing;

import com.store.order.Order;

public interface ShippingRule {
    double calculateShippingCost(Order order);
}