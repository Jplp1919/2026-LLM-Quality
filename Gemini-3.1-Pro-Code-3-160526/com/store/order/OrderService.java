package com.store.order;

import com.store.catalog.CatalogService;
import com.store.catalog.Product;
import com.store.customer.Customer;
import com.store.customer.CustomerService;
import com.store.exception.StoreBusinessException;
import com.store.inventory.StockService;
import com.store.pricing.Promotion;
import com.store.pricing.ShippingRule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderService {
    private Map<String, Order> orders = new HashMap<String, Order>();
    private CatalogService catalogService;
    private StockService stockService;
    private CustomerService customerService;

    public OrderService(CatalogService catalogService, StockService stockService, CustomerService customerService) {
        this.catalogService = catalogService;
        this.stockService = stockService;
        this.customerService = customerService;
    }

    public Order createOrder(String orderId, Cart cart) {
        if (cart.getItems().isEmpty()) {
            throw new StoreBusinessException("Each order must contain at least one order item.");
        }

        for (CartItem item : cart.getItems().values()) {
            if (stockService.getStock(item.getProductId()) < item.getQuantity()) {
                throw new StoreBusinessException("Insufficient stock for product: " + item.getProductId());
            }
        }

        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        double subtotal = 0.0;

        for (CartItem item : cart.getItems().values()) {
            stockService.decrease(item.getProductId(), item.getQuantity());
            Product product = catalogService.getProduct(item.getProductId());
            OrderItem orderItem = new OrderItem(product.getId(), item.getQuantity(), product.getUnitPrice());
            orderItems.add(orderItem);
            subtotal += orderItem.getSubtotal();
        }

        Order order = new Order(orderId, cart.getCustomerId(), orderItems, subtotal);
        orders.put(orderId, order);
        return order;
    }

    public void applyPromotion(Order order, Promotion promotion) {
        Customer customer = customerService.getCustomer(order.getCustomerId());
        double discount = promotion.calculateDiscount(order, customer);
        if (discount < 0) {
            throw new StoreBusinessException("Discount values cannot be negative.");
        }
        order.setPromotionDiscount(discount);
        calculateFinalTotal(order);
    }

    public void applyShipping(Order order, ShippingRule shippingRule) {
        double cost = shippingRule.calculateShippingCost(order);
        if (cost < 0) {
            throw new StoreBusinessException("Shipping values cannot be negative.");
        }
        order.setShippingCost(cost);
        calculateFinalTotal(order);
    }

    public void calculateFinalTotal(Order order) {
        double total = order.getSubtotal() - order.getPromotionDiscount() + order.getShippingCost();
        if (total < 0) {
            total = 0.0;
        }
        order.setFinalTotal(total);
    }

    public void cancelOrder(String orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new StoreBusinessException("Order not found.");
        }
        if (order.getStatus() == OrderStatus.PAID) {
            throw new StoreBusinessException("Paid orders cannot be cancelled.");
        }
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new StoreBusinessException("Order is already cancelled.");
        }
        
        for (OrderItem item : order.getItems()) {
            stockService.increase(item.getProductId(), item.getQuantity());
        }
        order.setStatus(OrderStatus.CANCELLED);
    }

    public Order getOrder(String orderId) {
        return orders.get(orderId);
    }

    public List<Order> getAllOrders() {
        return new ArrayList<Order>(orders.values());
    }
}