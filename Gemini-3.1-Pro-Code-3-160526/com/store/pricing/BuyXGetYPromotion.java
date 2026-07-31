package com.store.pricing;

import com.store.customer.Customer;
import com.store.order.Order;
import com.store.order.OrderItem;

public class BuyXGetYPromotion implements Promotion {
    private String targetProductId;
    private int buyX;
    private int getY;

    public BuyXGetYPromotion(String targetProductId, int buyX, int getY) {
        this.targetProductId = targetProductId;
        this.buyX = buyX;
        this.getY = getY;
    }

    public double calculateDiscount(Order order, Customer customer) {
        int count = 0;
        double price = 0.0;
        for (OrderItem item : order.getItems()) {
            if (item.getProductId().equals(targetProductId)) {
                count += item.getQuantity();
                price = item.getUnitPrice();
            }
        }
        int groupSize = buyX + getY;
        int freeItems = (count / groupSize) * getY;
        return freeItems * price;
    }
}