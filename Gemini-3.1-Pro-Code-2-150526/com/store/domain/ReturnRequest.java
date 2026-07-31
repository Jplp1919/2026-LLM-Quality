package com.store.domain;

import java.util.ArrayList;
import java.util.List;
import com.store.exception.ValidationException;

public class ReturnRequest {
    private String id;
    private Order order;
    private List<ReturnItem> items;
    private double refundTotal;

    public ReturnRequest(String id, Order order) {
        this.id = id;
        this.order = order;
        this.items = new ArrayList<ReturnItem>();
        this.refundTotal = 0.0;
    }

    public String getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public List<ReturnItem> getItems() {
        return items;
    }

    public void addItem(Product product, int quantity) {
        int boughtQuantity = 0;
        double unitPrice = 0.0;
        for (OrderItem oi : order.getItems()) {
            if (oi.getProduct().getId().equals(product.getId())) {
                boughtQuantity = oi.getQuantity();
                unitPrice = oi.getUnitPrice();
                break;
            }
        }
        if (quantity > boughtQuantity) {
            throw new ValidationException("Returned quantity cannot exceed bought quantity");
        }
        this.items.add(new ReturnItem(product, quantity));
        this.refundTotal += (quantity * unitPrice);
    }

    public double getRefundTotal() {
        return refundTotal;
    }
}