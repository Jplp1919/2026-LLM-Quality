package store.service;

import store.model.cashflow.CashflowEntry;
import store.model.cashflow.CashflowType;
import store.model.restock.RestockOperation;
import store.repository.DataStore;

public class RestockService {

    private DataStore dataStore;
    private StockService stockService;
    private CashflowService cashflowService;

    public RestockService(DataStore dataStore,
            StockService stockService,
            CashflowService cashflowService) {

        this.dataStore = dataStore;
        this.stockService = stockService;
        this.cashflowService = cashflowService;
    }

    public void registerRestock(RestockOperation operation) {
        stockService.increaseStock(
                operation.getProduct().getId(),
                operation.getQuantity());

        dataStore.getRestocks().add(operation);

        cashflowService.addEntry(new CashflowEntry(
                "CF-REST-" + operation.getId(),
                CashflowType.OUTFLOW,
                operation.getTotalCost(),
                "Restock " + operation.getId()));
    }
}