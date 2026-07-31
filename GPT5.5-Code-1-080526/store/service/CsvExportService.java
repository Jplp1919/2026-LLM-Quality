package store.service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import store.model.cashflow.CashflowEntry;
import store.model.catalog.Product;
import store.repository.DataStore;

public class CsvExportService {

    private DataStore dataStore;
    private StockService stockService;

    public CsvExportService(DataStore dataStore, StockService stockService) {
        this.dataStore = dataStore;
        this.stockService = stockService;
    }

    public void exportInventory() throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter("inventory.csv"));

        writer.println("productId,stock");

        Iterator iterator = dataStore.getProducts().values().iterator();

        while (iterator.hasNext()) {
            Product product = (Product) iterator.next();

            writer.println(product.getId() + "," +
                    stockService.getStock(product.getId()));
        }

        writer.close();

        System.out.println("Exported inventory.csv");
    }

    public void exportCashflow() throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter("cashflow.csv"));

        writer.println("id,type,amount,description");

        Iterator iterator = dataStore.getCashflowEntries().iterator();

        while (iterator.hasNext()) {
            CashflowEntry entry = (CashflowEntry) iterator.next();

            writer.println(
                    entry.getId() + "," +
                    entry.getType() + "," +
                    entry.getAmount() + "," +
                    entry.getDescription());
        }

        writer.close();

        System.out.println("Exported cashflow.csv");
    }
}