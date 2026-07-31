package com.store.promotions;

import com.store.domain.Order;

public interface Promotion {
    double calculateDiscount(Order order);
}