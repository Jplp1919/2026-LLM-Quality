package store.model.order;

import store.model.catalog.Product;

public class OrderItem {

    private Product product;
    private int quantity;
    private double unitPrice;

    public OrderItem(Product product, int quantity, double unitPrice) {
        if (product == null) {
            throw new IllegalArgumentException("Invalid product");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }

        if (unitPrice < 0) {
            throw new IllegalArgumentException("Invalid price");
        }

        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getSubtotal() {
        return quantity * unitPrice;
    }
}