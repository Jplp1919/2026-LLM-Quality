package br.com.store.domain;

public class CartItem {
private Product product;
private int quantity;

public CartItem(Product product, int quantity) {
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

public void setQuantity(int quantity) {
    if (quantity <= 0) {
        throw new IllegalArgumentException();
    }
    this.quantity = quantity;
}

public double getSubtotal() {
    return product.getUnitPrice() * quantity;
}
}