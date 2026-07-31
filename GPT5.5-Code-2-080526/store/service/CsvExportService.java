package store.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import store.model.cashflow.CashflowEntry;
import store.repository.DataStore;

public class CsvExportService {

    private DataStore dataStore;
    private StockService stockService;

    public CsvExportService(DataStore dataStore, StockService stockService) {
        this.dataStore = dataStore;
        this.stockService = stockService;
    }

    public void exportInventory() throws IOException {
        FileWriter writer = new FileWriter("inventory.csv");

        writer.write("productId,stock\n");

        Iterator<String> iterator = dataStore.getProducts().keySet().iterator();

        while (iterator.hasNext()) {
            String id = (String) iterator.next();

            writer.write(id + "," + stockService.getStock(id) + "\n");
        }

        writer.close();

        System.out.println("inventory.csv exported");
    }

    public void exportCashflow() throws IOException {
        FileWriter writer = new FileWriter("cashflow.csv");

        writer.write("id,type,amount,description\n");

        int i;

        for (i = 0; i < dataStore.getCashflows().size(); i++) {
            CashflowEntry entry = (CashflowEntry) dataStore.getCashflows().get(i);

            writer.write(
                    entry.getId()
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