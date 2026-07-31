package br.com.store.domain;

public class ReturnItem {
private Product product;
private int quantity;

public ReturnItem(Product product, int quantity) {
    if (quantity <= 0) {
        throw new IllegalArgumentException();
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
}