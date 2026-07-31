package store.service;

import java.util.Iterator;

import store.model.cashflow.CashflowEntry;
import store.model.cashflow.CashflowType;
import store.model.order.Order;
import store.model.order.OrderItem;
import store.model.order.OrderStatus;
import store.model.returns.ReturnItem;
import store.model.returns.ReturnRequest;
import store.repository.DataStore;

public class ReturnService {

    private DataStore dataStore;
    private StockService stockService;

    public ReturnService(DataStore dataStore, StockService stockService) {
        this.dataStore = dataStore;
        this.stockService = stockService;
    }

    public ReturnRequest createReturn(String id, Order order) {
        if (order.getStatus() != OrderStatus.PAID) {
            throw new IllegalArgumentException("Only paid orders can be returned");
        }

        ReturnRequest request = new ReturnRequest(id, order);
        dataStore.getReturns().add(request);

        return request;
    }

    public void addReturnedItem(ReturnRequest request, String productId, int quantity) {
        Iterator iterator = request.getOrder().getItems().iterator();

        while (iterator.hasNext()) {
            OrderItem item = (OrderItem) iterator.next();

            if (item.getProduct().getId().equals(productId)) {
                request.addItem(item, quantity);
                return;
            }
        }

        throw new IllegalArgumentException("Product not found");
    }

    public void processRefund(ReturnRequest request) {
        if (request.getItems().isEmpty()) {
            throw new IllegalArgumentException("Empty return");
        }

        Iterator iterator = request.getItems().iterator();

        while (iterator.hasNext()) {
            ReturnItem item = (ReturnItem) iterator.next();
            stockService.increaseStock(item.getProduct(), item.getQuantity());
        }

        dataStore.getCashflowEntries().add(
                new CashflowEntry(
                        "REFUND-" + request.getId(),
                        CashflowType.OUTFLOW,
                        request.calculateRefundTotal(),
                        "Refund"));
    }
}