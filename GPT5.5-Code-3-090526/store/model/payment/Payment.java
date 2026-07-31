package store.model.payment;

import store.model.order.Order;

public class Payment {
    private String id;
    private Order order;
    private double amount;

    public Payment(String id, Order order, double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Invalid payment");
        }

        this.id = id;
        this.order = order;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public double getAmount() {
        return amount;
    }
}