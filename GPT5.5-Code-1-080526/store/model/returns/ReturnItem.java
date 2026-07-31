package store.model.returns;

import store.model.catalog.Product;

public class ReturnItem {

    private Product product;
    private int quantity;

    public ReturnItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}