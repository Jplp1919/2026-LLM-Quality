package store.shipping;

import store.model.order.Order;

public class PickupShipping implements ShippingRule {

    public double calculateShipping(Order order) {
        return 0;
    }
}