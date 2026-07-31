package com.store.domain.promotion;

import com.store.domain.Order;
import com.store.domain.LoyaltyTier;

public class FixedDiscountPromotion implements Promotion {
private double discount;
private double threshold;
private LoyaltyTier requiredTier;

public FixedDiscountPromotion(double discount, double threshold, LoyaltyTier requiredTier) {
    this.discount = discount;
    this.threshold = threshold;
    this.requiredTier = requiredTier;
}

public double calculateDiscount(Order order) {
    if (order.getSubtotal() >= threshold && order.getCustomer().getTier() == requiredTier) {
        return discount;
    }
    return 0.0;
}
}