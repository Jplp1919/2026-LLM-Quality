package com.store.finance;

import java.util.ArrayList;
import java.util.List;

public class ReturnRequest {
    private String id;
    private String orderId;
    private List<ReturnItem> items;
    private double refundTotal;
    private boolean processed;

    public ReturnRequest(String id, String orderId) {
        this.id = id;
        this.orderId = orderId;
        this.items = new ArrayList<ReturnItem>();
        this.refundTotal = 0.0;
        this.processed = false;
    }

    public String getId() {
        return id;
    }

    public String getOrderId() {
        return orderId;
    }

    public List<ReturnItem> getItems() {
        return items;
    }

    public void addItem(ReturnItem item) {
        this.items.add(item);
    }

    public double getRefundTotal() {
        return refundTotal;
    }

    public void setRefundTotal(double refundTotal) {
        this.refundTotal = refundTotal;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }
}