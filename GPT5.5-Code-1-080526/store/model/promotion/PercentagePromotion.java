package store.model.promotion;

import store.model.order.Order;

public class PercentagePromotion implements Promotion {

    private String name;
    private double percentage;
    private double threshold;

    public PercentagePromotion(String name, double percentage, double threshold) {
        this.name = name;
        this.percentage = percentage;
        this.threshold = threshold;
    }

    public double calculateDiscount(Order order) {
        double subtotal = order.calculateSubtotal();

        if (subtotal >= threshold) {
            return subtotal * percentage / 100.0;
        }

        return 0;
    }

    public String getName() {
        return name;
    }
}