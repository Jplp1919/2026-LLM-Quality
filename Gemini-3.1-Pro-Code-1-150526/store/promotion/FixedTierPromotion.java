package store.promotion;

import store.model.LoyaltyTier;
import store.model.Order;

public class FixedTierPromotion implements Promotion {
    private LoyaltyTier requiredTier;
    private double threshold;
    private double discountAmount;

    public FixedTierPromotion(LoyaltyTier requiredTier, double threshold, double discountAmount) {
        this.requiredTier = requiredTier;
        this.threshold = threshold;
        this.discountAmount = discountAmount;
    }

    public double apply(Order order) {
        if (order.getCustomer().getTier() == requiredTier && order.getSubtotal() >= threshold) {
            return discountAmount;
        }
        return 0.0;
    }
}