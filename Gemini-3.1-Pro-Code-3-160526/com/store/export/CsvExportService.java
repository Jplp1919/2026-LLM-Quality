package com.store.export;

import com.store.exception.StoreBusinessException;
import com.store.finance.CashflowEntry;
import com.store.finance.CashflowService;
import com.store.inventory.StockService;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class CsvExportService {

    public void exportInventory(StockService stockService, String filename) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileWriter(filename));
            out.println("ProductId,StockQuantity");
            for (Map.Entry<String, Integer> entry : stockService.getAllStock().entrySet()) {
                out.println(entry.getKey() + "," + entry.getValue());
            }
        } catch (IOException e) {
            throw new StoreBusinessException("Failed to write inventory CSV: " + e.getMessage());
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public void exportCashflow(CashflowService cashflowService, String filename) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileWriter(filename));
            out.println("EntryId,Type,Amount,Description");
            for (CashflowEntry entry : cashflowService.getAllEntries()) {
                out.println(entry.getId() + "," + entry.getType() + "," + entry.getAmount() + "," + entry.getDescription());
            }
        } catch (IOException e) {
            throw new StoreBusinessException("Failed to write cashflow CSV: " + e.getMessage());
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}