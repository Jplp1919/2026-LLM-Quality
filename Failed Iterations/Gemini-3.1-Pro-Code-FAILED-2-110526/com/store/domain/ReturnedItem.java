package com.store.domain;

import com.store.exception.StoreBusinessException;

public class ReturnedItem {
private Product product;
private int quantity;

public ReturnedItem(Product product, int quantity) {
    if (quantity <= 0) {
        throw new StoreBusinessException("Return quantity must be positive");
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