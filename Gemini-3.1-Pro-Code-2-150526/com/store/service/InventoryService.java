package com.store.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.store.domain.Product;
import com.store.domain.RestockEntry;
import com.store.domain.Supplier;
import com.store.exception.ValidationException;

public class InventoryService {
    private Map<String, Integer> stock;
    private List<RestockEntry> restocks;
    private CashflowService cashflowService;
    private int restockCounter;

    public InventoryService(CashflowService cashflowService) {
        this.stock = new HashMap<String, Integer>();
        this.restocks = new ArrayList<RestockEntry>();
        this.cashflowService = cashflowService;
        this.restockCounter = 1;
    }

    public void initializeStock(String productId, int quantity) {
        if (quantity < 0) {
            throw new ValidationException("Stock cannot be negative");
        }
        stock.put(productId, quantity);
    }

    public void increaseStock(String productId, int quantity) {
        if (quantity < 0) {
            throw new ValidationException("Cannot increase stock by negative amount");
        }
        Integer current = stock.get(productId);
        if (current == null) current = 0;
        stock.put(productId, current + quantity);
    }

    public void decreaseStock(String productId, int quantity) {
        if (quantity < 0) {
            throw new ValidationException("Cannot decrease stock by negative amount");
        }
        Integer current = stock.get(productId);
        if (current == null) current = 0;
        if (current - quantity < 0) {
            throw new ValidationException("Operation would make stock negative for product: " + productId);
        }
        stock.put(productId, current - quantity);
    }

    public int getStock(String productId) {
        Integer current = stock.get(productId);
        return current == null ? 0 : current;
    }

    public RestockEntry restock(Product product, Supplier supplier, int quantity, double unitCost) {
        RestockEntry entry = new RestockEntry("RST" + (restockCounter++), product, supplier, quantity, unitCost);
        increaseStock(product.getId(), quantity);
        restocks.add(entry);
        cashflowService.registerOutflow("CFO_RST" + entry.getId(), entry.getTotalCost(), "Restock " + entry.getId());
        return entry;
    }

    public Map<String, Integer> getCompleteStock() {
        return stock;
    }

    public List<RestockEntry> getRestocks() {
        return restocks;
    }
}