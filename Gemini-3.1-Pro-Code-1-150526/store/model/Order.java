package store.model;

import java.util.List;

public class Order {
    private String id;
    private Customer customer;
    private List<OrderItem> items;
    private double subtotal;
    private double discount;
    private double shipping;
    private OrderStatus status;

    public Order(String id, Customer customer, List<OrderItem> items, double subtotal) {
        this.id = id;
        this.customer = customer;
        this.items = items;
        this.subtotal = subtotal;
        this.discount = 0.0;
        this.shipping = 0.0;
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

    public double getSubtotal() {
        return subtotal;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getShipping() {
        return shipping;
    }

    public void setShipping(double shipping) {
        this.shipping = shipping;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public double getTotal() {
        return subtotal - discount + shipping;
    }
}