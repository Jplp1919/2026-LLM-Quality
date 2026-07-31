package store.model.returns;

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

    public void addItem(OrderItem orderItem, int quantity) {
        if (quantity <= 0 || quantity > orderItem.getQuantity()) {
            throw new IllegalArgumentException("Invalid return quantity");
        }

        items.add(new ReturnItem(orderItem.getProduct(), quantity));
    }

    public double calculateRefundTotal() {
        double total = 0;

        Iterator iterator = order.getItems().iterator();

        while (iterator.hasNext()) {
            OrderItem orderItem = (OrderItem) iterator.next();

            Iterator returnIterator = items.iterator();

            while (returnIterator.hasNext()) {
                ReturnItem returnItem = (ReturnItem) returnIterator.next();

                if (returnItem.getProduct().getId().equals(orderItem.getProduct().getId())) {
                    total += returnItem.getQuantity() * orderItem.getUnitPrice();
                }
            }
        }

        return total;
    }
}