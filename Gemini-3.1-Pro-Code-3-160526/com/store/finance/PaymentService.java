package com.store.finance;

import com.store.exception.StoreBusinessException;
import com.store.order.Order;
import com.store.order.OrderStatus;

public class PaymentService {
    private CashflowService cashflowService;
    private int paymentCounter = 1;

    public PaymentService(CashflowService cashflowService) {
        this.cashflowService = cashflowService;
    }

    public void registerPayment(Order order) {
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new StoreBusinessException("Cancelled orders cannot be paid.");
        }
        if (order.getStatus() == OrderStatus.PAID) {
            throw new StoreBusinessException("Order is already paid.");
        }

        double amount = order.getFinalTotal();
        order.setStatus(OrderStatus.PAID);
        
        CashflowEntry entry = new CashflowEntry("PAY" + paymentCounter++, CashflowType.INFLOW, amount, "Payment for order " + order.getId());
        cashflowService.recordEntry(entry);
    }
}