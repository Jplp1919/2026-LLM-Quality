package store.model.returning;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import store.model.order.Order;
import store.model.order.OrderItem;

public class ReturnRequest {
    private String id;
    private Order order;
    private List items;

    public ReturnRequest(String id, Order order) {
        this.id = id;
        this.order = order;
        this.items = new ArrayList();
    }

    public String getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public List getItems() {
        return items;
    }

    public void addItem(String productId, int quantity) {
        Iterator iterator = order.getItems().iterator();

        while (iterator.hasNext()) {
            OrderItem item = (OrderItem) iterator.next();

            if (item.getProduct().getId().equals(productId)) {
                if (quantity > item.getQuantity()) {
                    throw new IllegalArgumentException("Invalid return quantity");
                }

                items.add(new ReturnItem(item.getProduct(), quantity));
                return;
            }
        }

        throw new IllegalArgumentException("Product not found");
    }

    public double getRefundTotal() {
        double total = 0;

        Iterator iterator = items.iterator();

        while (iterator.hasNext()) {
            ReturnItem item = (ReturnItem) iterator.next();

            Iterator orderIterator = order.getItems().iterator();

            while (orderIterator.hasNext()) {
                OrderItem orderItem = (OrderItem) orderIterator.next();

                if (orderItem.getProduct().getId().equals(item.getProduct().getId())) {
                    total += item.getQuantity() * orderItem.getUnitPrice();
                }
            }
        }

        return total;
    }
}