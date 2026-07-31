package store.service;

import store.model.cashflow.CashflowEntry;
import store.model.cashflow.CashflowType;
import store.model.returns.ReturnItem;
import store.model.returns.ReturnRequest;
import store.repository.DataStore;

public class ReturnService {

    private DataStore dataStore;
    private StockService stockService;
    private CashflowService cashflowService;

    public ReturnService(DataStore dataStore,
            StockService stockService,
            CashflowService cashflowService) {

        this.dataStore = dataStore;
        this.stockService = stockService;
        this.cashflowService = cashflowService;
    }

    public void processRefund(ReturnRequest request) {
        if (request.getItems().size() == 0) {
            throw new IllegalStateException("Empty return");
        }

        int i;

        for (i = 0; i < request.getItems().size(); i++) {
            ReturnItem item = (ReturnItem) request.getItems().get(i);

            stockService.increaseStock(
                    item.getOrderItem().getProduct().getId(),
                    item.getQuantity());
        }

        cashflowService.addEntry(new CashflowEntry(
                "CF-REF-" + request.getId(),
                CashflowType.OUTFLOW,
                request.getRefundTotal(),
                "Refund " + request.getId()));

        dataStore.getReturns().add(request);
    }
}