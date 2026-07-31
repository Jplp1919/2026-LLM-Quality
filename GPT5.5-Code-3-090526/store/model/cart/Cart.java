package store.model.cart;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import store.model.customer.Customer;
import store.model.product.Product;

public class Cart {
    private String id;
    private Customer customer;
    private List items;

    public Cart(String id, Customer customer) {
        this.id = id;
        this.customer = customer;
        this.items = new ArrayList();
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

    public void addProduct(Product product, int quantity) {
        if (!product.isActive()) {
            throw new IllegalArgumentException("Inactive product");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }

        CartItem item = findItem(product.getId());

        if (item == null) {
            items.add(new CartItem(product, quantity));
        } else {
            item.setQuantity(item.getQuantity() + quantity);
        }
    }

    public void updateProductQuantity(String productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }

        CartItem item = findItem(productId);

        if (item != null) {
            item.setQuantity(quantity);
        }
    }

    public void removeProduct(String productId) {
        Iterator iterator = items.iterator();

        while (iterator.hasNext()) {
            CartItem item = (CartItem) iterator.next();

            if (item.getProduct().getId().equals(productId)) {
                iterator.remove();
            }
        }
    }

    public double getSubtotal() {
        double total = 0;

        Iterator iterator = items.iterator();

        while (iterator.hasNext()) {
            CartItem item = (CartItem) iterator.next();
            total += item.getSubtotal();
        }

        return total;
    }

    private CartItem findItem(String productId) {
        Iterator iterator = items.iterator();

        while (iterator.hasNext()) {
            CartItem item = (CartItem) iterator.next();

            if (item.getProduct().getId().equals(productId)) {
                return item;
            }
        }

        return null;
    }
}