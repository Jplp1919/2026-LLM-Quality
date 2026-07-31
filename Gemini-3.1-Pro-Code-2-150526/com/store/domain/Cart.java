package com.store.domain;

import java.util.ArrayList;
import java.util.List;
import com.store.exception.ValidationException;

public class Cart {
    private Customer customer;
    private List<CartItem> items;

    public Cart(Customer customer) {
        this.customer = customer;
        this.items = new ArrayList<CartItem>();
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void addProduct(Product product, int quantity) {
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new CartItem(product, quantity));
    }

    public void updateQuantity(String productId, int quantity) {
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(quantity);
                return;
            }
        }
        throw new ValidationException("Product not found in cart");
    }

    public void removeProduct(String productId) {
        CartItem toRemove = null;
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(productId)) {
                toRemove = item;
                break;
            }
        }
        if (toRemove != null) {
            items.remove(toRemove);
        }
    }

    public double getSubtotalPreview() {
        double total = 0.0;
        for (CartItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }
}