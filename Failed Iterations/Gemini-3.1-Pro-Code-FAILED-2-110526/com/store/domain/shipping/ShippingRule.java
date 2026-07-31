package com.store.domain.shipping;

import com.store.domain.Order;

public interface ShippingRule {
double calculateShipping(Order order);
}