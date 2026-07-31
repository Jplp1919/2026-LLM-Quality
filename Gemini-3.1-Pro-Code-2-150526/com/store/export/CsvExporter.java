package com.store.export;

import java.io.FileWriter;
import java.util.Map;
import com.store.domain.CashflowEntry;
import com.store.service.CashflowService;
import com.store.service.InventoryService;

public class CsvExporter {
    public static void exportInventory(InventoryService inventoryService, String filename) throws Exception {
        FileWriter writer = null;
        try {
            writer = new FileWriter(filename);
            writer.write("ProductId,StockQuantity\n");
            Map<String, Integer> stock = inventoryService.getCompleteStock();
            for (String pid : stock.keySet()) {
                writer.write(pid + "," + stock.get(pid) + "\n");
            }
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public static void exportCashflow(CashflowService cashflowService, String filename) throws Exception {
        FileWriter writer = null;
        try {
            writer = new FileWriter(filename);
            writer.write("EntryId,Type,Amount,Description\n");
            for (CashflowEntry e : cashflowService.getEntries()) {
                writer.write(e.getId() + "," + e.getType().name() + "," + e.getAmount() + "," + e.getDescription() + "\n");
            }
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}