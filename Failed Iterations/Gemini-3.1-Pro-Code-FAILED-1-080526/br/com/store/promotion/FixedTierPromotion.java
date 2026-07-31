package br.com.store.promotion;

import br.com.store.domain.LoyaltyTier;
import br.com.store.domain.Order;

public class FixedTierPromotion implements Promotion {
private double discount;
private LoyaltyTier tier;
private double threshold;

public FixedTierPromotion(double discount, LoyaltyTier tier, double threshold) {
    this.discount = discount;
    this.tier = tier;
    this.threshold = threshold;
}

public double calculateDiscount(Order order) {
    if (order.getCustomer().getLoyaltyTier() == tier && order.getSubtotal() >= threshold) {
        return discount;
    }
    return 0.0;
}
}