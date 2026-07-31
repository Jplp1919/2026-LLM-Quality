package br.com.store.service;

import br.com.store.domain.Cart;
import br.com.store.domain.Order;
import br.com.store.domain.OrderItem;
import br.com.store.domain.OrderStatus;
import br.com.store.domain.ReturnItem;
import br.com.store.domain.ReturnRequest;
import br.com.store.domain.ShippingRule;
import br.com.store.promotion.Promotion;

import java.util.ArrayList;
import java.util.List;

public class OrderService {
private InventoryService inventoryService;
private FinanceService financeService;
private List orders;
private List returns;
private int returnCounter;

public OrderService(InventoryService inventoryService, FinanceService financeService) {
    this.inventoryService = inventoryService;
    this.financeService = financeService;
    this.orders = new ArrayList<Order>();
    this.returns = new ArrayList<ReturnRequest>();
    this.returnCounter = 1;
}

public Order createOrder(String id, Cart cart) {
    for (br.com.store.domain.CartItem item : cart.getItems()) {
        if (inventoryService.getStock(item.getProduct()) < item.getQuantity()) {
            throw new IllegalStateException();
        }
    }
    Order order = new Order(id, cart);
    for (OrderItem item : order.getItems()) {
        inventoryService.decreaseStock(item.getProduct(), item.getQuantity());
    }
    orders.add(order);
    return order;
}

public void applyPromotion(Order order, Promotion promotion) {
    if (order.getStatus() != OrderStatus.CREATED) {
        throw new IllegalStateException();
    }
    double discount = promotion.calculateDiscount(order);
    order.setPromotionDiscount(discount);
}

public void applyShipping(Order order, ShippingRule shippingRule) {
    if (order.getStatus() != OrderStatus.CREATED) {
        throw new IllegalStateException();
    }
    order.setShippingRule(shippingRule);
}

public void payOrder(Order order) {
    if (order.getStatus() != OrderStatus.CREATED) {
        throw new IllegalStateException();
    }
    order.setStatus(OrderStatus.PAID);
    financeService.registerInflow(order.getFinalTotal(), "Payment for Order " + order.getId());
}

public void cancelOrder(Order order) {
    if (order.getStatus() != OrderStatus.CREATED) {
        throw new IllegalStateException();
    }
    order.setStatus(OrderStatus.CANCELLED);
    for (OrderItem item : order.getItems()) {
        inventoryService.increaseStock(item.getProduct(), item.getQuantity());
    }
}

public ReturnRequest createReturnRequest(Order order) {
    return new ReturnRequest("RET" + returnCounter++, order);
}

public void processReturn(ReturnRequest returnRequest) {
    if (returnRequest.isProcessed()) {
        throw new IllegalStateException();
    }
    if (returnRequest.getItems().isEmpty()) {
        throw new IllegalStateException();
    }
    for (ReturnItem item : returnRequest.getItems()) {
        inventoryService.increaseStock(item.getProduct(), item.getQuantity());
    }
    financeService.registerOutflow(returnRequest.getRefundTotal(), "Refund for Return " + returnRequest.getId());
    returnRequest.setProcessed(true);
    returnRequest.getOrder().setStatus(OrderStatus.RETURNED);
    returns.add(returnRequest);
}

public List<Order> getOrders() {
    return orders;
}

public List<ReturnRequest> getReturns() {
    return returns;
}
}