package store.promotion;

import store.model.customer.LoyaltyTier;
import store.model.order.Order;

public class FixedDiscountPromotion implements Promotion {
    private double threshold;
    private double discount;
    private LoyaltyTier requiredTier;

    public FixedDiscountPromotion(double threshold, double discount, LoyaltyTier requiredTier) {
        this.threshold = threshold;
        this.discount = discount;
        this.requiredTier = requiredTier;
    }

    public double calculateDiscount(Order order) {
        if (order.getSubtotal() >= threshold
                && order.getCustomer().getLoyaltyTier() == requiredTier) {
            return discount;
        }

        return 0;
    }
}