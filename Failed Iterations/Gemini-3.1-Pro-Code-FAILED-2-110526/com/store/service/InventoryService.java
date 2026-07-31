package com.store.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.store.exception.StoreBusinessException;

public class InventoryService {
private Map<String, Integer> stock = new HashMap<String, Integer>();

public void increaseStock(String productId, int quantity) {
    if (quantity < 0) {
        throw new StoreBusinessException("Cannot increase stock by negative amount");
    }
    int current = getStock(productId);
    stock.put(productId, current + quantity);
}

public void decreaseStock(String productId, int quantity) {
    if (quantity < 0) {
        throw new StoreBusinessException("Cannot decrease stock by negative amount");
    }
    int current = getStock(productId);
    if (current < quantity) {
        throw new StoreBusinessException("Stock quantity cannot become negative");
    }
    stock.put(productId, current - quantity);
}

public int getStock(String productId) {
    Integer current = stock.get(productId);
    return current == null ? 0 : current;
}

public Map<String, Integer> getAllStock() {
    return new HashMap<String, Integer>(stock);
}

public void exportToCsv(String filename) {
    FileWriter writer = null;
    try {
        writer = new FileWriter(filename);
        writer.write("ProductId,StockQuantity\n");
        for (Map.Entry<String, Integer> entry : stock.entrySet()) {
            writer.write(entry.getKey() + "," + entry.getValue() + "\n");
        }
    } catch (IOException e) {
        throw new StoreBusinessException("Error exporting inventory to CSV");
    } finally {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
            }
        }
    }
}
}