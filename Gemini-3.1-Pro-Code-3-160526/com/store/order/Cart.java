package com.store.order;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    private String customerId;
    private Map<String, CartItem> items = new HashMap<String, CartItem>();

    public Cart(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public Map<String, CartItem> getItems() {
        return items;
    }
}