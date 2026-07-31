package br.com.store.domain;

import java.util.ArrayList;
import java.util.List;

public class Order {
private String id;
private Customer customer;
private List items;
private OrderStatus status;
private double promotionDiscount;
private ShippingRule shippingRule;

public Order(String id, Cart cart) {
    if (cart.getItems().isEmpty()) {
        throw new IllegalStateException();
    }
    this.id = id;
    this.customer = cart.getCustomer();
    this.items = new ArrayList<OrderItem>();
    for (CartItem cartItem : cart.getItems()) {
        this.items.add(new OrderItem(cartItem.getProduct(), cartItem.getQuantity(), cartItem.getProduct().getUnitPrice()));
    }
    this.status = OrderStatus.CREATED;
    this.promotionDiscount = 0.0;
}

public String getId() {
    return id;
}

public Customer getCustomer() {
    return customer;
}

public List<OrderItem> getItems() {
    return items;
}

public OrderStatus getStatus() {
    return status;
}

public void setStatus(OrderStatus status) {
    this.status = status;
}

public double getPromotionDiscount() {
    return promotionDiscount;
}

public void setPromotionDiscount(double promotionDiscount) {
    if (promotionDiscount < 0) {
        throw new IllegalArgumentException();
    }
    this.promotionDiscount = promotionDiscount;
}

public ShippingRule getShippingRule() {
    return shippingRule;
}

public void setShippingRule(ShippingRule shippingRule) {
    this.shippingRule = shippingRule;
}

public double getSubtotal() {
    double subtotal = 0.0;
    for (OrderItem item : items) {
        subtotal += item.getSubtotal();
    }
    return subtotal;
}

public double getShippingCost() {
    if (shippingRule != null) {
        return shippingRule.getCost();
    }
    return 0.0;
}

public double getFinalTotal() {
    return getSubtotal() - promotionDiscount + getShippingCost();
}
}