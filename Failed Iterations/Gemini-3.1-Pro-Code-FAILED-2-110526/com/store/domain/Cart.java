package com.store.domain;

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

public double getSubtotalPreview() {
    double subtotal = 0.0;
    for (CartItem item : items) {
        subtotal += item.getProduct().getUnitPrice() * item.getQuantity();
    }
    return subtotal;
}
}