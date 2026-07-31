package com.store.domain;

import com.store.exception.ValidationException;

public class CartItem {
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        if (quantity <= 0) {
            throw new ValidationException("Cart item quantity must be positive");
        }
        if (!product.isActive()) {
            throw new ValidationException("Cannot add inactive product to cart");
        }
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new ValidationException("Cart item quantity must be positive");
        }
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return product.getUnitPrice() * quantity;
    }
}