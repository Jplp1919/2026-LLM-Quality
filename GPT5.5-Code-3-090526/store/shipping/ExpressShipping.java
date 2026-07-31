package store.shipping;

import store.model.order.Order;

public class ExpressShipping implements ShippingRule {
    private double cost;

    public ExpressShipping(double cost) {
        this.cost = cost;
    }

    public double calculateShipping(Order order) {
        return cost;
    }
}