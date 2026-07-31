package store.shipping;

import store.model.Order;

public class ExpressShipping implements ShippingRule {
    private double cost;

    public ExpressShipping(double cost) {
        this.cost = cost;
    }

    public double calculate(Order order) {
        return cost;
    }
}