package com.store.service;

import java.util.ArrayList;
import java.util.List;
import com.store.domain.Cart;
import com.store.domain.CartItem;
import com.store.domain.Order;
import com.store.domain.OrderItem;
import com.store.domain.OrderStatus;
import com.store.domain.promotion.Promotion;
import com.store.domain.shipping.ShippingRule;
import com.store.exception.StoreBusinessException;

public class OrderService {
private InventoryService inventoryService;
private List orders = new ArrayList();

public OrderService(InventoryService inventoryService) {
    this.inventoryService = inventoryService;
}

public Order createOrderFromCart(String orderId, Cart cart) {
    List<OrderItem> orderItems = new ArrayList<OrderItem>();
    for (CartItem ci : cart.getItems()) {
        orderItems.add(new OrderItem(ci.getProduct(), ci.getQuantity(), ci.getProduct().getUnitPrice()));
    }
    Order order = new Order(orderId, cart.getCustomer(), orderItems);
    for (OrderItem oi : order.getItems()) {
        inventoryService.decreaseStock(oi.getProduct().getId(), oi.getQuantity());
    }
    orders.add(order);
    return order;
}

public void applyPromotion(Order order, Promotion promotion) {
    if (order.getStatus() != OrderStatus.PENDING) {
        throw new StoreBusinessException("Can only apply promotions to pending orders");
    }
    double discount = promotion.calculateDiscount(order);
    order.setPromotionDiscount(discount);
}

public void applyShipping(Order order, ShippingRule shippingRule) {
    if (order.getStatus() != OrderStatus.PENDING) {
        throw new StoreBusinessException("Can only apply shipping to pending orders");
    }
    double cost = shippingRule.calculateShipping(order);
    order.setShippingCost(cost);
}

public void cancelOrder(Order order) {
    if (order.getStatus() == OrderStatus.PAID) {
        throw new StoreBusinessException("Paid orders cannot be cancelled");
    }
    if (order.getStatus() == OrderStatus.CANCELLED) {
        return;
    }
    order.setStatus(OrderStatus.CANCELLED);
    for (OrderItem oi : order.getItems()) {
        inventoryService.increaseStock(oi.getProduct().getId(), oi.getQuantity());
    }
}

public Order getOrder(String orderId) {
    for (Order o : orders) {
        if (o.getId().equals(orderId)) {
            return o;
        }
    }
    return null;
}

public List<Order> getAllOrders() {
    return new ArrayList<Order>(orders);
}
}