package store.model.returns;

import java.util.ArrayList;
import java.util.List;

import store.model.order.Order;
import store.model.order.OrderItem;
import store.model.order.OrderStatus;

public class ReturnRequest {

    private String id;
    private Order order;
    private List<ReturnItem> items;

    public ReturnRequest(String id, Order order) {
        if (order.getStatus() != OrderStatus.PAID) {
            throw new IllegalArgumentException("Order not paid");
        }

        this.id = id;
        this.order = order;
        this.items = new ArrayList<ReturnItem>();
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

    public void addItem(String productId, int quantity) {
        List<OrderItem> orderItems = order.getItems();
        int i;

        for (i = 0; i < orderItems.size(); i++) {
            OrderItem orderItem = (OrderItem) orderItems.get(i);

            if (orderItem.getProduct().getId().equals(productId)) {
                items.add(new ReturnItem(orderItem, quantity));
                return;
            }
        }

        throw new IllegalArgumentException("Product not found");
    }

    public double getRefundTotal() {
        if (items.size() == 0) {
            throw new IllegalStateException("Empty return");
        }

        double total = 0;
        int i;

        for (i = 0; i < items.size(); i++) {
            total += ((ReturnItem) items.get(i)).getRefundValue();
        }

        return total;
    }
}