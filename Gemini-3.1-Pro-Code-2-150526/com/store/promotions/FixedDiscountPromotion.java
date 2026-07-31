package com.store.promotions;

import com.store.domain.Order;
import com.store.domain.enums.CustomerTier;

public class FixedDiscountPromotion implements Promotion {
    private double discount;
    private double threshold;
    private CustomerTier requiredTier;

    public FixedDiscountPromotion(double discount, double threshold, CustomerTier requiredTier) {
        this.discount = discount;
        this.threshold = threshold;
        this.requiredTier = requiredTier;
    }

    public double calculateDiscount(Order order) {
        double subtotal = order.getSubtotal();
        if (subtotal >= threshold && order.getCustomer().getTier() == requiredTier) {
            return discount;
        }
        return 0.0;
    }
}