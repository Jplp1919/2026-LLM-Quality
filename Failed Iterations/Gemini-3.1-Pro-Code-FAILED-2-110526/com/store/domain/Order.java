package com.store.domain;

import java.util.ArrayList;
import java.util.List;
import com.store.exception.StoreBusinessException;

public class Order {
private String id;
private Customer customer;
private List items;
private double subtotal;
private double promotionDiscount;
private double shippingCost;
private OrderStatus status;

public Order(String id, Customer customer, List<OrderItem> items) {
    if (items == null || items.isEmpty()) {
        throw new StoreBusinessException("Order must contain at least one item");
    }
    this.id = id;
    this.customer = customer;
    this.items = new ArrayList<OrderItem>(items);
    this.status = OrderStatus.PENDING;
    this.promotionDiscount = 0.0;
    this.shippingCost = 0.0;
    this.subtotal = 0.0;
    for (OrderItem item : items) {
        this.subtotal += item.getSubtotal();
    }
}

public String getId() {
    return id;
}

public Customer getCustomer() {
    return customer;
}

public List<OrderItem> getItems() {
    return items;
}

public double getSubtotal() {
    return subtotal;
}

public double getPromotionDiscount() {
    return promotionDiscount;
}

public void setPromotionDiscount(double promotionDiscount) {
    if (promotionDiscount < 0) {
        throw new StoreBusinessException("Discount cannot be negative");
    }
    this.promotionDiscount = promotionDiscount;
}

public double getShippingCost() {
    return shippingCost;
}

public void setShippingCost(double shippingCost) {
    if (shippingCost < 0) {
        throw new StoreBusinessException("Shipping cost cannot be negative");
    }
    this.shippingCost = shippingCost;
}

public double getFinalTotal() {
    return subtotal - promotionDiscount + shippingCost;
}

public OrderStatus getStatus() {
    return status;
}

public void setStatus(OrderStatus status) {
    this.status = status;
}
}