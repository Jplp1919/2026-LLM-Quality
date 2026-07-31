package com.store.domain;

import java.util.List;
import com.store.domain.enums.OrderStatus;
import com.store.exception.ValidationException;

public class Order {
    private String id;
    private Customer customer;
    private List<OrderItem> items;
    private OrderStatus status;
    private double promotionDiscount;
    private double shippingCost;

    public Order(String id, Customer customer, List<OrderItem> items) {
        if (items == null || items.size() == 0) {
            throw new ValidationException("Order must contain at least one item");
        }
        this.id = id;
        this.customer = customer;
        this.items = items;
        this.status = OrderStatus.PENDING;
        this.promotionDiscount = 0.0;
        this.shippingCost = 0.0;
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

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public double getPromotionDiscount() {
        return promotionDiscount;
    }

    public void setPromotionDiscount(double promotionDiscount) {
        if (promotionDiscount < 0) {
            throw new ValidationException("Discount cannot be negative");
        }
        this.promotionDiscount = promotionDiscount;
    }

    public double getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(double shippingCost) {
        if (shippingCost < 0) {
            throw new ValidationException("Shipping cost cannot be negative");
        }
        this.shippingCost = shippingCost;
    }

    public double getSubtotal() {
        double total = 0.0;
        for (OrderItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    public double getFinalTotal() {
        double sub = getSubtotal();
        double discount = Math.min(promotionDiscount, sub);
        return sub - discount + shippingCost;
    }
}