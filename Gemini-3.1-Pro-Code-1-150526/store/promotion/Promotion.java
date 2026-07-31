package store.promotion;

import store.model.Order;

public interface Promotion {
    double apply(Order order);
}