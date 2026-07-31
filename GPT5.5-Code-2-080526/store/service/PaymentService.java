package store.service;

import store.model.cashflow.CashflowEntry;
import store.model.cashflow.CashflowType;
import store.model.order.Order;
import store.model.order.OrderStatus;
import store.model.payment.Payment;

public class PaymentService {

    private CashflowService cashflowService;

    public PaymentService(CashflowService cashflowService) {
        this.cashflowService = cashflowService;
    }

    public Payment registerPayment(String id, Order order) {
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Cancelled order");
        }

        Payment payment = new Payment(id, order, order.getFinalTotal());

        order.markPaid();

        cashflowService.addEntry(new CashflowEntry(
                "CF-PAY-" + id,
                CashflowType.INFLOW,
                payment.getAmount(),
                "Payment " + id));

        return payment;
    }
}