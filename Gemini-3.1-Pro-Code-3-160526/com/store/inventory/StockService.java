package com.store.inventory;

import com.store.exception.StoreBusinessException;
import java.util.HashMap;
import java.util.Map;

public class StockService {
    private Map<String, Integer> stock = new HashMap<String, Integer>();

    public void initializeStock(String productId, int quantity) {
        if (quantity < 0) {
            throw new StoreBusinessException("Stock quantity cannot be negative.");
        }
        stock.put(productId, quantity);
    }

    public int getStock(String productId) {
        Integer qty = stock.get(productId);
        if (qty == null) {
            return 0;
        }
        return qty.intValue();
    }

    public void increase(String productId, int amount) {
        if (amount < 0) {
            throw new StoreBusinessException("Amount cannot be negative.");
        }
        int current = getStock(productId);
        stock.put(productId, current + amount);
    }

    public void decrease(String productId, int amount) {
        if (amount < 0) {
            throw new StoreBusinessException("Amount cannot be negative.");
        }
        int current = getStock(productId);
        if (current - amount < 0) {
            throw new StoreBusinessException("Stock cannot become negative for product: " + productId);
        }
        stock.put(productId, current - amount);
    }

    public Map<String, Integer> getAllStock() {
        return new HashMap<String, Integer>(stock);
    }
}