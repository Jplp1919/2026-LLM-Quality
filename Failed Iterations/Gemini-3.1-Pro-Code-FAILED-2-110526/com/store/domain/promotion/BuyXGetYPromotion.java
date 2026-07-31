package com.store.domain.promotion;

import com.store.domain.Order;
import com.store.domain.OrderItem;
import com.store.domain.Product;

public class BuyXGetYPromotion implements Promotion {
private Product targetProduct;
private int buyQuantity;
private int freeQuantity;

public BuyXGetYPromotion(Product targetProduct, int buyQuantity, int freeQuantity) {
    this.targetProduct = targetProduct;
    this.buyQuantity = buyQuantity;
    this.freeQuantity = freeQuantity;
}

public double calculateDiscount(Order order) {
    double totalDiscount = 0.0;
    int requiredGroupSize = buyQuantity + freeQuantity;
    for (OrderItem item : order.getItems()) {
        if (item.getProduct().getId().equals(targetProduct.getId())) {
            int groups = item.getQuantity() / requiredGroupSize;
            totalDiscount += groups * freeQuantity * item.getUnitPrice();
        }
    }
    return totalDiscount;
}
}