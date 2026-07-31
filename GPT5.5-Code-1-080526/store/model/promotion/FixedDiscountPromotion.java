package store.model.promotion;

import store.model.customer.LoyaltyTier;
import store.model.order.Order;

public class FixedDiscountPromotion implements Promotion {

    private String name;
    private double discount;
    private double threshold;
    private LoyaltyTier loyaltyTier;

    public FixedDiscountPromotion(String name, double discount, double threshold, LoyaltyTier loyaltyTier) {
        this.name = name;
        this.discount = discount;
        this.threshold = threshold;
        this.loyaltyTier = loyaltyTier;
    }

    public double calculateDiscount(Order order) {
        if (order.getCustomer().getLoyaltyTier() == loyaltyTier
                && order.calculateSubtotal() >= threshold) {
            return discount;
        }

        return 0;
    }

    public String getName() {
        return name;
    }
}