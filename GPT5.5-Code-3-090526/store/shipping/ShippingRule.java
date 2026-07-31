package store.shipping;

import store.model.order.Order;

public interface ShippingRule {
    double calculateShipping(Order order);
}