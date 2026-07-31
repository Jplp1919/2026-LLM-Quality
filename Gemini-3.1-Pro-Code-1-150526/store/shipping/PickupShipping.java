package store.shipping;

import store.model.Order;

public class PickupShipping implements ShippingRule {
    public double calculate(Order order) {
        return 0.0;
    }
}