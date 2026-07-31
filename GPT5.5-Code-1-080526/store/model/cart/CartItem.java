package store.model.cart;

import store.model.catalog.Product;

public class CartItem {

    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Invalid product");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void updateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return product.getUnitPrice() * quantity;
    }
}