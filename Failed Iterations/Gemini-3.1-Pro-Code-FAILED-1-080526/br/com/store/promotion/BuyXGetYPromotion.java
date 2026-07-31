package br.com.store.promotion;

import br.com.store.domain.Order;
import br.com.store.domain.OrderItem;
import br.com.store.domain.Product;

public class BuyXGetYPromotion implements Promotion {
private Product product;
private int x;
private int y;

public BuyXGetYPromotion(Product product, int x, int y) {
    this.product = product;
    this.x = x;
    this.y = y;
}

public double calculateDiscount(Order order) {
    int quantity = 0;
    double price = 0.0;
    for (OrderItem item : order.getItems()) {
        if (item.getProduct().getId().equals(product.getId())) {
            quantity += item.getQuantity();
            price = item.getUnitPrice();
        }
    }
    int block = x + y;
    int sets = quantity / block;
    int freeItems = sets * y;
    return freeItems * price;
}
}