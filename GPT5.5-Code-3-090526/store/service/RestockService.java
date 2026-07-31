package store.service;

import store.model.cashflow.CashflowEntry;
import store.model.cashflow.CashflowType;
import store.model.product.Product;
import store.model.restock.RestockOperation;
import store.model.supplier.Supplier;
import store.repository.DataStore;

public class RestockService {
    private DataStore dataStore;

    public RestockService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public RestockOperation restock(String id,
            Product product,
            Supplier supplier,
            int quantity,
            double unitCost) {

        RestockOperation operation = new RestockOperation(
                id, product, supplier, quantity, unitCost);

        dataStore.increaseStock(product.getId(), quantity);

        dataStore.addRestock(operation);

        dataStore.addCashflow(new CashflowEntry(
                "CF-RESTOCK-" + id,
                CashflowType.OUTFLOW,
                operation.getTotalCost(),
                "Restock " + id));

        return operation;
    }
}