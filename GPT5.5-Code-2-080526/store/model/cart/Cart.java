package store.model.cart;

import java.util.ArrayList;
import java.util.List;

import store.model.customer.Customer;
import store.model.product.Product;

public class Cart {

    private String id;
    private Customer customer;
    private List<CartItem> items;

    public Cart(String id, Customer customer) {
        this.id = id;
        this.customer = customer;
        this.items = new ArrayList<CartItem>();
    }

    public String getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<CartItem> getItems() {
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

    public void updateQuantity(String productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }

        CartItem item = findItem(productId);

        if (item != null) {
            item.setQuantity(quantity);
        }
    }

    public void removeProduct(String productId) {
        CartItem item = findItem(productId);

        if (item != null) {
            items.remove(item);
        }
    }

    public CartItem findItem(String productId) {
        int i;

        for (i = 0; i < items.size(); i++) {
            CartItem item = (CartItem) items.get(i);

            if (item.getProduct().getId().equals(productId)) {
                return item;
            }
        }

        return null;
    }

    public double getSubtotal() {
        double total = 0;
        int i;

        for (i = 0; i < items.size(); i++) {
            total += ((CartItem) items.get(i)).getSubtotal();
        }

        return total;
    }
}