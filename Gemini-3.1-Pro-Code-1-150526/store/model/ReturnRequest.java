package store.model;

import java.util.List;

public class ReturnRequest {
    private String id;
    private Order order;
    private List<ReturnItem> items;
    private double refundTotal;

    public ReturnRequest(String id, Order order, List<ReturnItem> items, double refundTotal) {
        this.id = id;
        this.order = order;
        this.items = items;
        this.refundTotal = refundTotal;
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
}