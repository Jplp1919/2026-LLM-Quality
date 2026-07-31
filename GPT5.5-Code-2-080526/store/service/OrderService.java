package store.service;

import java.util.ArrayList;
import java.util.List;

import store.model.cart.Cart;
import store.model.cart.CartItem;
import store.model.order.Order;
import store.model.order.OrderItem;
import store.model.order.OrderStatus;
import store.model.promotion.Promotion;
import store.model.shipping.ShippingRule;
import store.repository.DataStore;

public class OrderService {

    private DataStore dataStore;
    private StockService stockService;

    public OrderService(DataStore dataStore, StockService stockService) {
        this.dataStore = dataStore;
        this.stockService = stockService;
    }

    public Order createOrder(String id, Cart cart) {
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        List<CartItem> cartItems = cart.getItems();

        int i;

        for (i = 0; i < cartItems.size(); i++) {
            CartItem cartItem = (CartItem) cartItems.get(i);

            stockService.decreaseStock(cartItem.getProduct().getId(), cartItem.getQuantity());

            orderItems.add(new OrderItem(
                    cartItem.getProduct(),
                    cartItem.getQuantity(),
                    cartItem.getProduct().getUnitPrice()));
        }

        Order order = new Order(id, cart.getCustomer(), orderItems);

        dataStore.getOrders().add(order);

        return order;
    }

    public void applyPromotion(Order order, Promotion promotion) {
        order.applyPromotion(promotion);
    }

    public void applyShipping(Order order, ShippingRule shippingRule) {
        order.applyShipping(shippingRule);
    }

    public void cancelOrder(Order order) {
        if (order.getStatus() == OrderStatus.PAID) {
            throw new IllegalStateException("Paid order");
        }

        order.cancel();

        List<OrderItem> items = order.getItems();
        int i;

        for (i = 0; i < items.size(); i++) {
            OrderItem item = (OrderItem) items.get(i);

            stockService.increaseStock(item.getProduct().getId(), item.getQuantity());
        }
    }
}