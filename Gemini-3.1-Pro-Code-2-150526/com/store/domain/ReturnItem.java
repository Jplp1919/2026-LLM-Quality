package com.store.domain;

import com.store.exception.ValidationException;

public class ReturnItem {
    private Product product;
    private int quantity;

    public ReturnItem(Product product, int quantity) {
        if (quantity <= 0) {
            throw new ValidationException("Returned quantity must be positive");
        }
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}