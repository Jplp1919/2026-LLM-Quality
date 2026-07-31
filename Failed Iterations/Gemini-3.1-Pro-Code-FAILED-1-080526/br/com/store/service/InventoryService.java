package br.com.store.service;

import br.com.store.domain.Product;
import br.com.store.domain.Restock;
import br.com.store.domain.Supplier;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryService {
private Map<String, Integer> stock;
private FinanceService financeService;
private List restocks;
private int restockCounter;

public InventoryService(FinanceService financeService) {
    this.stock = new HashMap<String, Integer>();
    this.financeService = financeService;
    this.restocks = new ArrayList<Restock>();
    this.restockCounter = 1;
}

public void initializeStock(Product product, int quantity) {
    if (quantity < 0) throw new IllegalArgumentException();
    stock.put(product.getId(), quantity);
}

public int getStock(Product product) {
    Integer quantity = stock.get(product.getId());
    return quantity == null ? 0 : quantity;
}

public void increaseStock(Product product, int quantity) {
    if (quantity <= 0) throw new IllegalArgumentException();
    int current = getStock(product);
    stock.put(product.getId(), current + quantity);
}

public void decreaseStock(Product product, int quantity) {
    if (quantity <= 0) throw new IllegalArgumentException();
    int current = getStock(product);
    if (current < quantity) throw new IllegalStateException();
    stock.put(product.getId(), current - quantity);
}

public Restock performRestock(Product product, Supplier supplier, int quantity, double unitCost) {
    Restock restock = new Restock("RST" + restockCounter++, product, supplier, quantity, unitCost);
    increaseStock(product, quantity);
    financeService.registerOutflow(restock.getTotalCost(), "Restock " + restock.getId());
    restocks.add(restock);
    return restock;
}

public List<Restock> getRestocks() {
    return restocks;
}

public Map<String, Integer> getAllStock() {
    return new HashMap<String, Integer>(stock);
}

public void exportInventoryCsv(String filename) {
    PrintWriter pw = null;
    try {
        pw = new PrintWriter(new FileWriter(filename));
        pw.println("ProductID,Quantity");
        for (Map.Entry<String, Integer> entry : stock.entrySet()) {
            pw.println(entry.getKey() + "," + entry.getValue());
        }
    } catch (IOException e) {
        throw new RuntimeException(e);
    } finally {
        if (pw != null) {
            pw.close();
        }
    }
}
}