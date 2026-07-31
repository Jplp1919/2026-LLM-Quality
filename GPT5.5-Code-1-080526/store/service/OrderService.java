package store.service;

import java.util.Iterator;

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
        if (cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Empty cart");
        }

        Order order = new Order(id, cart.getCustomer());

        Iterator iterator = cart.getItems().iterator();

        while (iterator.hasNext()) {
            CartItem cartItem = (CartItem) iterator.next();

            stockService.decreaseStock(cartItem.getProduct(), cartItem.getQuantity());

            order.addItem(new OrderItem(
                    cartItem.getProduct(),
                    cartItem.getQuantity(),
                    cartItem.getProduct().getUnitPrice()));
        }

        dataStore.getOrders().add(order);

        return order;
    }

    public void applyPromotion(Order order, Promotion promotion) {
        order.setPromotionDiscount(promotion.calculateDiscount(order));
    }

    public void applyShipping(Order order, ShippingRule shippingRule) {
        order.setShippingCost(shippingRule.calculateShipping());
    }

    public void cancelOrder(Order order) {
        if (order.getStatus() == OrderStatus.PAID) {
            throw new IllegalArgumentException("Paid order cannot be cancelled");
        }

        Iterator iterator = order.getItems().iterator();

        while (iterator.hasNext()) {
            OrderItem item = (OrderItem) iterator.next();
            stockService.increaseStock(item.getProduct(), item.getQuantity());
        }

        order.cancel();
    }
}