package br.com.store.promotion;

import br.com.store.domain.Order;

public interface Promotion {
double calculateDiscount(Order order);
}