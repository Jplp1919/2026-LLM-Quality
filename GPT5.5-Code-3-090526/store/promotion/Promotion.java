package store.promotion;

import store.model.order.Order;

public interface Promotion {
    double calculateDiscount(Order order);
}