package com.store.domain.promotion;

import com.store.domain.Order;

public interface Promotion {
double calculateDiscount(Order order);
}