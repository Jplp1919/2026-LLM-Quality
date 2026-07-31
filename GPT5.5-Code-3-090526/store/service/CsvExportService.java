package store.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import store.model.cashflow.CashflowEntry;
import store.repository.DataStore;

public class CsvExportService {
    private DataStore dataStore;

    public CsvExportService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public void exportInventory() throws IOException {
        FileWriter writer = new FileWriter("inventory.csv");

        writer.write("productId,stock\n");

        Iterator iterator = dataStore.getProductsMap().values().iterator();

        while (iterator.hasNext()) {
            store.model.product.Product product =
                    (store.model.product.Product) iterator.next();

            writer.write(product.getId()
                    + ","
                    + dataStore.getStock(product.getId())
                    + "\n");
        }

        writer.close();

        System.out.println("inventory.csv exported");
    }

    public void exportCashflow() throws IOException {
        FileWriter writer = new FileWriter("cashflow.csv");

        writer.write("id,type,amount,description\n");

        Iterator iterator = dataStore.getCashflows().iterator();

        while (iterator.hasNext()) {
            CashflowEntry entry = (CashflowEntry) iterator.next();

            writer.write(entry.getId()
                    + ","
                    + entry.getType()
                    + ","
                    + entry.getAmount()
                    + ","
                    + entry.getDescription()
                    + "\n");
        }

        writer.close();

        System.out.println("cashflow.csv exported");
    }
}