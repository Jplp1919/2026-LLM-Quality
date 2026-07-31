package com.store.pricing;

import com.store.customer.Customer;
import com.store.order.Order;

public class PercentagePromotion implements Promotion {
    private double percentage;
    private double threshold;

    public PercentagePromotion(double percentage, double threshold) {
        this.percentage = percentage;
        this.threshold = threshold;
    }

    public double calculateDiscount(Order order, Customer customer) {
        if (order.getSubtotal() >= threshold) {
            return order.getSubtotal() * (percentage / 100.0);
        }
        return 0.0;
    }
}