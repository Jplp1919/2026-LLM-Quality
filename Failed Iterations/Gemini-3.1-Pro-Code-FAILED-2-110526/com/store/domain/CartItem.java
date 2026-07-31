package com.store.domain;

import com.store.exception.StoreBusinessException;

public class CartItem {
private Product product;
private int quantity;

public CartItem(Product product, int quantity) {
    if (quantity <= 0) {
        throw new StoreBusinessException("Quantity must be positive");
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

public void setQuantity(int quantity) {
    if (quantity <= 0) {
        throw new StoreBusinessException("Quantity must be positive");
    }
    this.quantity = quantity;
}
}