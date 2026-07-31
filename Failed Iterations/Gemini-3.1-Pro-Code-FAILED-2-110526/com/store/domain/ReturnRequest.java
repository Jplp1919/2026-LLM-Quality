package com.store.domain;

import java.util.ArrayList;
import java.util.List;
import com.store.exception.StoreBusinessException;

public class ReturnRequest {
private String id;
private Order order;
private List items;
private double refundTotal;

public ReturnRequest(String id, Order order, List<ReturnedItem> items, double refundTotal) {
    if (items == null || items.isEmpty()) {
        throw new StoreBusinessException("A return must contain at least one returned item");
    }
    if (refundTotal < 0) {
        throw new StoreBusinessException("Refund total cannot be negative");
    }
    this.id = id;
    this.order = order;
    this.items = new ArrayList<ReturnedItem>(items);
    this.refundTotal = refundTotal;
}

public String getId() {
    return id;
}

public Order getOrder() {
    return order;
}

public List<ReturnedItem> getItems() {
    return items;
}

public double getRefundTotal() {
    return refundTotal;
}
}