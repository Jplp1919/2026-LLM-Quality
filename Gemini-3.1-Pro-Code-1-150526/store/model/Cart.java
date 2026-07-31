package store.model;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    private Customer customer;
    private Map<Product, Integer> items;

    public Cart(Customer customer) {
        this.customer = customer;
        this.items = new HashMap<Product, Integer>();
    }

    public Customer getCustomer() {
        return customer;
    }

    public Map<Product, Integer> getItems() {
        return items;
    }

    public double getSubtotal() {
        double subtotal = 0.0;
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            subtotal += entry.getKey().getUnitPrice() * entry.getValue();
        }
        return subtotal;
    }
}