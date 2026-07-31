package com.store.promotions;

import com.store.domain.Order;
import com.store.domain.OrderItem;
import com.store.domain.Product;

public class BuyXGetYPromotion implements Promotion {
    private Product product;
    private int buy;
    private int get;

    public BuyXGetYPromotion(Product product, int buy, int get) {
        this.product = product;
        this.buy = buy;
        this.get = get;
    }

    public double calculateDiscount(Order order) {
        for (OrderItem item : order.getItems()) {
            if (item.getProduct().getId().equals(product.getId())) {
                int freeItems = (item.getQuantity() / (buy + get)) * get;
                return freeItems * item.getUnitPrice();
            }
        }
        return 0.0;
    }
}