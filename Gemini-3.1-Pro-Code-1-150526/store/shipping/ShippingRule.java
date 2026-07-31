package store.shipping;

import store.model.Order;

public interface ShippingRule {
    double calculate(Order order);
}