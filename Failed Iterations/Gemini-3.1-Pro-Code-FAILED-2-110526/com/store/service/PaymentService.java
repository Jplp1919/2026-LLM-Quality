package com.store.service;

import com.store.domain.CashflowType;
import com.store.domain.Order;
import com.store.domain.OrderStatus;
import com.store.exception.StoreBusinessException;

public class PaymentService {
private CashflowService cashflowService;
private int paymentCounter = 1;

public PaymentService(CashflowService cashflowService) {
    this.cashflowService = cashflowService;
}

public void registerPayment(Order order) {
    if (order.getStatus() == OrderStatus.CANCELLED) {
        throw new StoreBusinessException("Cancelled orders cannot be paid");
    }
    if (order.getStatus() == OrderStatus.PAID) {
        throw new StoreBusinessException("Order is already paid");
    }
    order.setStatus(OrderStatus.PAID);
    String paymentId = "PAY" + (paymentCounter++);
    cashflowService.recordEntry(paymentId, order.getFinalTotal(), CashflowType.INFLOW, "Payment for order " + order.getId());
}
}