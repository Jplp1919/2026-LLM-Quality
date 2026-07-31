package com.store.domain.shipping;

import com.store.domain.Order;

public class PickupShipping implements ShippingRule {
public double calculateShipping(Order order) {
return 0.0;
}
}