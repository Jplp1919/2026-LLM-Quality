package store.shipping;

import store.model.order.Order;

public class StandardShipping implements ShippingRule {
    private double cost;

    public StandardShipping(double cost) {
        this.cost = cost;
    }

    public double calculateShipping(Order order) {
        return cost;
    }
}