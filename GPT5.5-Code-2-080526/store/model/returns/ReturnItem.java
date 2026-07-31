package store.model.returns;

import store.model.order.OrderItem;

public class ReturnItem {

    private OrderItem orderItem;
    private int quantity;

    public ReturnItem(OrderItem orderItem, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }

        if (quantity > orderItem.getQuantity()) {
            throw new IllegalArgumentException("Invalid return quantity");
        }

        this.orderItem = orderItem;
        this.quantity = quantity;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getRefundValue() {
        return quantity * orderItem.getUnitPrice();
    }
}