package br.com.store.promotion;

import br.com.store.domain.Order;

public class PercentagePromotion implements Promotion {
private double percentage;
private double threshold;

public PercentagePromotion(double percentage, double threshold) {
    this.percentage = percentage;
    this.threshold = threshold;
}

public double calculateDiscount(Order order) {
    if (order.getSubtotal() >= threshold) {
        return order.getSubtotal() * percentage;
    }
    return 0.0;
}
}