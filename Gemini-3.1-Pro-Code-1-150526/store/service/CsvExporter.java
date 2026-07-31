package store.service;

import store.model.CashflowEntry;
import store.model.Product;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CsvExporter {

    public void exportInventory(Map<String, Product> products, Map<String, Integer> stock) {
        String filename = "inventory.csv";
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter(new File(filename)));
            pw.println("ProductId,ProductName,Category,UnitPrice,StockQuantity");
            for (Product p : products.values()) {
                Integer qty = stock.get(p.getId());
                if (qty == null) qty = 0;
                pw.println(p.getId() + "," + p.getName() + "," + p.getCategory().getName() + "," + p.getUnitPrice() + "," + qty);
            }
            System.out.println("Exported inventory data to " + filename);
        } catch (IOException e) {
            System.out.println("Failed to export inventory: " + e.getMessage());
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    public void exportCashflow(List<CashflowEntry> entries) {
        String filename = "cashflow.csv";
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter(new File(filename)));
            pw.println("EntryId,Type,Amount,Description");
            for (CashflowEntry entry : entries) {
                pw.println(entry.getId() + "," + entry.getType() + "," + entry.getAmount() + "," + entry.getDescription());
            }
            System.out.println("Exported cashflow data to " + filename);
        } catch (IOException e) {
            System.out.println("Failed to export cashflow: " + e.getMessage());
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }
}