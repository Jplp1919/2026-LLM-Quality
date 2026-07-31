package br.com.store.domain;

import java.util.ArrayList;
import java.util.List;

public class Cart {
private Customer customer;
private List items;

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

public void addItem(Product product, int quantity) {
    if (!product.isActive()) {
        throw new IllegalStateException();
    }
    if (quantity <= 0) {
        throw new IllegalArgumentException();
    }
    for (CartItem item : items) {
        if (item.getProduct().getId().equals(product.getId())) {
            item.setQuantity(item.getQuantity() + quantity);
            return;
        }
    }
    items.add(new CartItem(product, quantity));
}

public void updateItemQuantity(Product product, int quantity) {
    if (quantity <= 0) {
        throw new IllegalArgumentException();
    }
    for (CartItem item : items) {
        if (item.getProduct().getId().equals(product.getId())) {
            item.setQuantity(quantity);
            return;
        }
    }
    throw new IllegalArgumentException();
}

public void removeItem(Product product) {
    CartItem toRemove = null;
    for (CartItem item : items) {
        if (item.getProduct().getId().equals(product.getId())) {
            toRemove = item;
            break;
        }
    }
    if (toRemove != null) {
        items.remove(toRemove);
    }
}

public double getSubtotal() {
    double subtotal = 0.0;
    for (CartItem item : items) {
        subtotal += item.getSubtotal();
    }
    return subtotal;
}
}