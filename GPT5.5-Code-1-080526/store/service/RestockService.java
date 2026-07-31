package store.service;

import store.model.cashflow.CashflowEntry;
import store.model.cashflow.CashflowType;
import store.model.catalog.Product;
import store.model.restock.RestockOperation;
import store.model.supplier.Supplier;
import store.repository.DataStore;

public class RestockService {

    private DataStore dataStore;
    private StockService stockService;

    public RestockService(DataStore dataStore, StockService stockService) {
        this.dataStore = dataStore;
        this.stockService = stockService;
    }

    public RestockOperation restock(
            String id,
            Product product,
            Supplier supplier,
            int quantity,
            double unitCost) {

        RestockOperation operation =
                new RestockOperation(id, product, supplier, quantity, unitCost);

        stockService.increaseStock(product, quantity);

        dataStore.getRestocks().add(operation);

        dataStore.getCashflowEntries().add(
                new CashflowEntry(
                        "RESTOCK-" + id,
                        CashflowType.OUTFLOW,
                        operation.getTotalCost(),
                        "Restock"));

        return operation;
    }
}