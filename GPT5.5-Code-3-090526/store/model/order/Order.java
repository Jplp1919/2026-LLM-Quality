package store.model.order;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import store.model.customer.Customer;
import store.promotion.Promotion;
import store.shipping.ShippingRule;

public class Order {
    private String id;
    private Customer customer;
    private List items;
    private double promotionDiscount;
    private double shippingCost;
    private OrderStatus status;

    public Order(String id, Customer customer) {
        this.id = id;
        this.customer = customer;
        this.items = new ArrayList();
        this.status = OrderStatus.CREATED;
    }

    public String getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List getItems() {
        return items;
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public double getSubtotal() {
        double total = 0;

        Iterator iterator = items.iterator();

        while (iterator.hasNext()) {
            OrderItem item = (OrderItem) iterator.next();
            total += item.getSubtotal();
        }

        return total;
    }

    public void applyPromotion(Promotion promotion) {
        promotionDiscount = promotion.calculateDiscount(this);
    }

    public void applyShipping(ShippingRule shippingRule) {
        shippingCost = shippingRule.calculateShipping(this);
    }

    public double getFinalTotal() {
        return getSubtotal() - promotionDiscount + shippingCost;
    }

    public double getPromotionDiscount() {
        return promotionDiscount;
    }

    public double getShippingCost() {
        return shippingCost;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void markPaid() {
        status = OrderStatus.PAID;
    }

    public void cancel() {
        status = OrderStatus.CANCELLED;
    }
}