package store.service;

import java.util.Iterator;

import store.model.cart.Cart;
import store.model.cart.CartItem;
import store.model.order.Order;
import store.model.order.OrderItem;
import store.model.order.OrderStatus;
import store.repository.DataStore;

public class OrderService {
    private DataStore dataStore;

    public OrderService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public Order createOrder(String id, Cart cart) {
        if (cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Empty cart");
        }

        Order order = new Order(id, cart.getCustomer());

        Iterator iterator = cart.getItems().iterator();

        while (iterator.hasNext()) {
            CartItem cartItem = (CartItem) iterator.next();

            dataStore.decreaseStock(cartItem.getProduct().getId(),
                    cartItem.getQuantity());

            order.addItem(new OrderItem(
                    cartItem.getProduct(),
                    cartItem.getQuantity(),
                    cartItem.getProduct().getUnitPrice()));
        }

        dataStore.addOrder(order);

        return order;
    }

    public void cancelOrder(Order order) {
        if (order.getStatus() == OrderStatus.PAID) {
            throw new IllegalArgumentException("Paid order cannot be cancelled");
        }

        Iterator iterator = order.getItems().iterator();

        while (iterator.hasNext()) {
            OrderItem item = (OrderItem) iterator.next();

            dataStore.increaseStock(item.getProduct().getId(),
                    item.getQuantity());
        }

        order.cancel();
    }
}