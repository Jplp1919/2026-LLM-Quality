package store.service;

import java.util.Map;

import store.model.catalog.Product;
import store.repository.DataStore;

public class StockService {

    private DataStore dataStore;

    public StockService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public void increaseStock(Product product, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }

        Map stock = dataStore.getStock();

        Integer current = (Integer) stock.get(product.getId());

        if (current == null) {
            current = new Integer(0);
        }

        stock.put(product.getId(), new Integer(current.intValue() + quantity));
    }

    public void decreaseStock(Product product, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }

        int current = getStock(product.getId());

        if (current - quantity < 0) {
            throw new IllegalArgumentException("Negative stock");
        }

        dataStore.getStock().put(product.getId(), new Integer(current - quantity));
    }

    public int getStock(String productId) {
        Integer value = (Integer) dataStore.getStock().get(productId);

        if (value == null) {
            return 0;
        }

        return value.intValue();
    }
}