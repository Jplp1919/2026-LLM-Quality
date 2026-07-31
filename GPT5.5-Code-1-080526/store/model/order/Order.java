package store.model.order;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import store.model.customer.Customer;

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

    public OrderStatus getStatus() {
        return status;
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public double calculateSubtotal() {
        double total = 0;

        Iterator iterator = items.iterator();

        while (iterator.hasNext()) {
            OrderItem item = (OrderItem) iterator.next();
            total += item.getSubtotal();
        }

        return total;
    }

    public double getPromotionDiscount() {
        return promotionDiscount;
    }

    public void setPromotionDiscount(double promotionDiscount) {
        if (promotionDiscount < 0) {
            throw new IllegalArgumentException("Invalid discount");
        }
        this.promotionDiscount = promotionDiscount;
    }

    public double getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(double shippingCost) {
        if (shippingCost < 0) {
            throw new IllegalArgumentException("Invalid shipping");
        }
        this.shippingCost = shippingCost;
    }

    public double calculateFinalTotal() {
        return calculateSubtotal() - promotionDiscount + shippingCost;
    }

    public void markPaid() {
        status = OrderStatus.PAID;
    }

    public void cancel() {
        status = OrderStatus.CANCELLED;
    }
}