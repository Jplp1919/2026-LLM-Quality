package com.store.pricing;

import com.store.customer.Customer;
import com.store.customer.LoyaltyTier;
import com.store.order.Order;

public class FixedDiscountPromotion implements Promotion {
    private double discount;
    private double threshold;
    private LoyaltyTier requiredTier;

    public FixedDiscountPromotion(double discount, double threshold, LoyaltyTier requiredTier) {
        this.discount = discount;
        this.threshold = threshold;
        this.requiredTier = requiredTier;
    }

    public double calculateDiscount(Order order, Customer customer) {
        if (customer.getTier() == requiredTier && order.getSubtotal() >= threshold) {
            return discount;
        }
        return 0.0;
    }
}