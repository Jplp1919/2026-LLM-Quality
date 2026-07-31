package com.store.finance;

public class ReturnItem {
    private String productId;
    private int quantity;

    public ReturnItem(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}