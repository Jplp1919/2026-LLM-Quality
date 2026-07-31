package store.service;

import java.util.Iterator;

import store.model.cashflow.CashflowEntry;
import store.model.cashflow.CashflowType;
import store.model.order.OrderStatus;
import store.model.returning.ReturnItem;
import store.model.returning.ReturnRequest;
import store.repository.DataStore;

public class ReturnService {
    private DataStore dataStore;

    public ReturnService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public ReturnRequest createReturn(String id,
            store.model.order.Order order) {

        if (order.getStatus() != OrderStatus.PAID) {
            throw new IllegalArgumentException("Only paid orders");
        }

        ReturnRequest request = new ReturnRequest(id, order);

        dataStore.addReturn(request);

        return request;
    }

    public void processRefund(ReturnRequest request) {
        if (request.getItems().isEmpty()) {
            throw new IllegalArgumentException("Empty return");
        }

        Iterator iterator = request.getItems().iterator();

        while (iterator.hasNext()) {
            ReturnItem item = (ReturnItem) iterator.next();

            dataStore.increaseStock(item.getProduct().getId(),
                    item.getQuantity());
        }

        dataStore.addCashflow(new CashflowEntry(
                "CF-REF-" + request.getId(),
                CashflowType.OUTFLOW,
                request.getRefundTotal(),
                "Refund for " + request.getId()));
    }
}