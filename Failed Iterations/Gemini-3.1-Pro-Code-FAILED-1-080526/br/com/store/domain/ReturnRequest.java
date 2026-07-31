package br.com.store.domain;

import java.util.ArrayList;
import java.util.List;

public class ReturnRequest {
private String id;
private Order order;
private List items;
private double refundTotal;
private boolean processed;

public ReturnRequest(String id, Order order) {
    if (order.getStatus() != OrderStatus.PAID) {
        throw new IllegalStateException();
    }
    this.id = id;
    this.order = order;
    this.items = new ArrayList<ReturnItem>();
    this.refundTotal = 0.0;
    this.processed = false;
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

public double getRefundTotal() {
    return refundTotal;
}

public boolean isProcessed() {
    return processed;
}

public void addReturnItem(Product product, int quantity) {
    if (quantity <= 0) {
        throw new IllegalArgumentException();
    }
    int orderedQty = 0;
    double unitPrice = 0.0;
    for (OrderItem oi : order.getItems()) {
        if (oi.getProduct().getId().equals(product.getId())) {
            orderedQty += oi.getQuantity();
            unitPrice = oi.getUnitPrice();
        }
    }
    if (orderedQty == 0) {
        throw new IllegalArgumentException();
    }
    int currentReturnQty = 0;
    for (ReturnItem ri : items) {
        if (ri.getProduct().getId().equals(product.getId())) {
            currentReturnQty += ri.getQuantity();
        }
    }
    if (currentReturnQty + quantity > orderedQty) {
        throw new IllegalArgumentException();
    }
    items.add(new ReturnItem(product, quantity));
    refundTotal += (quantity * unitPrice);
}

public void setProcessed(boolean processed) {
    this.processed = processed;
}
}