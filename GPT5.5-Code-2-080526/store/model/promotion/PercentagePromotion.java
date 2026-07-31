package store.model.promotion;

import store.model.order.Order;

public class PercentagePromotion implements Promotion {

    private String name;
    private double threshold;
    private double percentage;

    public PercentagePromotion(String name, double threshold, double percentage) {
        this.name = name;
        this.threshold = threshold;
        this.percentage = percentage;
    }

    public String getName() {
        return name;
    }

    public double calculateDiscount(Order order) {
        if (order.getSubtotal() >= threshold) {
            return order.getSubtotal() * percentage;
        }

        return 0;
    }
}