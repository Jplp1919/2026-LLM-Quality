package com.store.promotions;

import com.store.domain.Order;

public class PercentagePromotion implements Promotion {
    private double threshold;
    private double percentage;

    public PercentagePromotion(double threshold, double percentage) {
        this.threshold = threshold;
        this.percentage = percentage;
    }

    public double calculateDiscount(Order order) {
        double subtotal = order.getSubtotal();
        if (subtotal >= threshold) {
            return subtotal * (percentage / 100.0);
        }
        return 0.0;
    }
}