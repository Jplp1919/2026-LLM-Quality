package store.model.order;

import java.util.ArrayList;
import java.util.List;

import store.model.customer.Customer;
import store.model.promotion.Promotion;
import store.model.shipping.ShippingRule;

public class Order {

    private String id;
    private Customer customer;
    private List<OrderItem> items;
    private Promotion promotion;
    private double promotionDiscount;
    private ShippingRule shippingRule;
    private double shippingCost;
    private OrderStatus status;

    public Order(String id, Customer customer, List<OrderItem> items) {
        if (items == null || items.size() == 0) {
            throw new IllegalArgumentException("Empty order");
        }

        this.id = id;
        this.customer = customer;
        this.items = new ArrayList<OrderItem>(items);
        this.status = OrderStatus.CREATED;
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

    public double getSubtotal() {
        double total = 0;
        int i;

        for (i = 0; i < items.size(); i++) {
            total += ((OrderItem) items.get(i)).getSubtotal();
        }

        return total;
    }

    public void applyPromotion(Promotion promotion) {
        this.promotion = promotion;
        this.promotionDiscount = promotion.calculateDiscount(this);
    }

    public void applyShipping(ShippingRule shippingRule) {
        this.shippingRule = shippingRule;
        this.shippingCost = shippingRule.calculateShipping();
    }

    public double getPromotionDiscount() {
        return promotionDiscount;
    }

    public double getShippingCost() {
        return shippingCost;
    }

    public double getFinalTotal() {
        return getSubtotal() - promotionDiscount + shippingCost;
    }

    public void markPaid() {
        if (status == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Cancelled order");
        }

        status = OrderStatus.PAID;
    }

    public void cancel() {
        if (status == OrderStatus.PAID) {
            throw new IllegalStateException("Paid order");
        }

        status = OrderStatus.CANCELLED;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public ShippingRule getShippingRule() {
        return shippingRule;
    }
}