package store.promotion;

import store.model.order.Order;

public class PercentagePromotion implements Promotion {
    private double threshold;
    private double percentage;

    public PercentagePromotion(double threshold, double percentage) {
        this.threshold = threshold;
        this.percentage = percentage;
    }

    public double calculateDiscount(Order order) {
        if (order.getSubtotal() >= threshold) {
            return order.getSubtotal() * percentage / 100.0;
        }

        return 0;
    }
}