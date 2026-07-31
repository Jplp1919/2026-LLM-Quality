package com.store.domain;

import com.store.exception.StoreBusinessException;

public class OrderItem {
private Product product;
private int quantity;
private double unitPrice;

public OrderItem(Product product, int quantity, double unitPrice) {
    if (quantity <= 0) {
        throw new StoreBusinessException("Quantity must be positive");
    }
    if (unitPrice < 0) {
        throw new StoreBusinessException("Unit price cannot be negative");
    }
    this.product = product;
    this.quantity = quantity;
    this.unitPrice = unitPrice;
}

public Product getProduct() {
    return product;
}

public int getQuantity() {
    return quantity;
}

public double getUnitPrice() {
    return unitPrice;
}

public double getSubtotal() {
    return quantity * unitPrice;
}
}