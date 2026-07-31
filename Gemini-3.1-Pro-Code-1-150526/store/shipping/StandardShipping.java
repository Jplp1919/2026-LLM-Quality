package store.shipping;

import store.model.Order;

public class StandardShipping implements ShippingRule {
    private double cost;

    public StandardShipping(double cost) {
        this.cost = cost;
    }

    public double calculate(Order order) {
        return cost;
    }
}