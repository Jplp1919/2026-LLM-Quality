package com.store.service;

import java.util.ArrayList;
import java.util.List;
import com.store.domain.Cart;
import com.store.domain.CartItem;
import com.store.domain.Order;
import com.store.domain.OrderItem;
import com.store.domain.ReturnItem;
import com.store.domain.ReturnRequest;
import com.store.domain.enums.OrderStatus;
import com.store.exception.ValidationException;
import com.store.promotions.Promotion;
import com.store.shipping.ShippingRule;

public class OrderService {
    private InventoryService inventoryService;
    private CashflowService cashflowService;
    private List<Order> orders;
    private List<ReturnRequest> returns;
    private int returnCounter;

    public OrderService(InventoryService inventoryService, CashflowService cashflowService) {
        this.inventoryService = inventoryService;
        this.cashflowService = cashflowService;
        this.orders = new ArrayList<Order>();
        this.returns = new ArrayList<ReturnRequest>();
        this.returnCounter = 1;
    }

    public Order createOrder(String orderId, Cart cart) {
        if (cart.getItems().size() == 0) {
            throw new ValidationException("Cannot create order from empty cart");
        }
        List<OrderItem> items = new ArrayList<OrderItem>();
        for (CartItem ci : cart.getItems()) {
            items.add(new OrderItem(ci.getProduct(), ci.getQuantity(), ci.getProduct().getUnitPrice()));
            inventoryService.decreaseStock(ci.getProduct().getId(), ci.getQuantity());
        }
        Order order = new Order(orderId, cart.getCustomer(), items);
        orders.add(order);
        return order;
    }

    public void applyPromotion(Order order, Promotion promotion) {
        double discount = promotion.calculateDiscount(order);
        order.setPromotionDiscount(discount);
    }

    public void applyShipping(Order order, ShippingRule rule) {
        order.setShippingCost(rule.getCost());
    }

    public void payOrder(Order order) {
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new ValidationException("Cancelled orders cannot be paid");
        }
        order.setStatus(OrderStatus.PAID);
        cashflowService.registerInflow("CFI_PAY_" + order.getId(), order.getFinalTotal(), "Payment for order " + order.getId());
    }

    public void cancelOrder(Order order) {
        if (order.getStatus() == OrderStatus.PAID || order.getStatus() == OrderStatus.RETURNED) {
            throw new ValidationException("Paid orders cannot be cancelled");
        }
        order.setStatus(OrderStatus.CANCELLED);
        for (OrderItem oi : order.getItems()) {
            inventoryService.increaseStock(oi.getProduct().getId(), oi.getQuantity());
        }
    }

    public ReturnRequest createReturnRequest(Order order) {
        if (order.getStatus() != OrderStatus.PAID) {
            throw new ValidationException("Only paid orders can be returned");
        }
        return new ReturnRequest("RET" + (returnCounter++), order);
    }

    public void processReturn(ReturnRequest req) {
        if (req.getItems().size() == 0) {
            throw new ValidationException("A return must contain at least one returned item");
        }
        req.getOrder().setStatus(OrderStatus.RETURNED);
        for (ReturnItem ri : req.getItems()) {
            inventoryService.increaseStock(ri.getProduct().getId(), ri.getQuantity());
        }
        cashflowService.registerOutflow("CFO_REF_" + req.getId(), req.getRefundTotal(), "Refund for return " + req.getId());
        returns.add(req);
    }

    public List<Order> getOrders() {
        return orders;
    }

    public List<ReturnRequest> getReturns() {
        return returns;
    }
}