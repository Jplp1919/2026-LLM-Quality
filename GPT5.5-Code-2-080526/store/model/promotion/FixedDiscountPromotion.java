package store.model.promotion;

import store.model.customer.LoyaltyTier;
import store.model.order.Order;

public class FixedDiscountPromotion implements Promotion {

    private String name;
    private double threshold;
    private double discount;
    private LoyaltyTier requiredTier;

    public FixedDiscountPromotion(String name, double threshold, double discount, LoyaltyTier requiredTier) {
        this.name = name;
        this.threshold = threshold;
        this.discount = discount;
        this.requiredTier = requiredTier;
    }

    public String getName() {
        return name;
    }

    public double calculateDiscount(Order order) {
        if (order.getSubtotal() >= threshold
                && order.getCustomer().getLoyaltyTier() == requiredTier) {
            return discount;
        }

        return 0;
    }
}