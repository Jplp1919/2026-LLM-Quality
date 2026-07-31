package store.model.promotion;

import store.model.order.Order;

public interface Promotion {

    String getName();

    double calculateDiscount(Order order);
}