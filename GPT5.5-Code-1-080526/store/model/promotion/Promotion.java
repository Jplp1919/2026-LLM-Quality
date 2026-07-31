package store.model.promotion;

import store.model.order.Order;

public interface Promotion {

    double calculateDiscount(Order order);

    String getName();
}