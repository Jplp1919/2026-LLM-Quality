package com.store.order;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private String id;
    private String customerId;
    private List<OrderItem> items;
    private OrderStatus status;
    private double subtotal;
    private double promotionDiscount;
    private double shippingCost;
    private double finalTotal;

    public Order(String id, String customerId, List<OrderItem> items, double subtotal) {
        this.id = id;
        this.customerId = customerId;
        this.items = new ArrayList<OrderItem>(items);
        this.subtotal = subtotal;
        this.status = OrderStatus.PENDING;
        this.promotionDiscount = 0.0;
        this.shippingCost = 0.0;
        this.finalTotal = subtotal;
    }

    public String getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
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

    public double getSubtotal() {
        return subtotal;
    }

    public double getPromotionDiscount() {
        return promotionDiscount;
    }

    public void setPromotionDiscount(double promotionDiscount) {
        this.promotionDiscount = promotionDiscount;
    }

    public double getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(double shippingCost) {
        this.shippingCost = shippingCost;
    }

    public double getFinalTotal() {
        return finalTotal;
    }

    public void setFinalTotal(double finalTotal) {
        this.finalTotal = finalTotal;
    }
}