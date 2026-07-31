package com.store.pricing;

import com.store.customer.Customer;
import com.store.order.Order;

public interface Promotion {
    double calculateDiscount(Order order, Customer customer);
}