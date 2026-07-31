package store.service;

import store.model.cashflow.CashflowEntry;
import store.model.cashflow.CashflowType;
import store.model.order.Order;
import store.model.order.OrderStatus;
import store.model.payment.Payment;
import store.repository.DataStore;

public class PaymentService {
    private DataStore dataStore;

    public PaymentService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public Payment payOrder(String paymentId, Order order) {
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalArgumentException("Cancelled order");
        }

        Payment payment = new Payment(paymentId, order,
                order.getFinalTotal());

        order.markPaid();

        dataStore.addCashflow(new CashflowEntry(
                "CF-PAY-" + paymentId,
                CashflowType.INFLOW,
                payment.getAmount(),
                "Payment for " + order.getId()));

        return payment;
    }
}