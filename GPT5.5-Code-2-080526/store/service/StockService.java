package store.service;

import store.repository.DataStore;

public class StockService {

    private DataStore dataStore;

    public StockService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public void increaseStock(String productId, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }

        Integer current = (Integer) dataStore.getStock().get(productId);

        if (current == null) {
            current = new Integer(0);
        }

        dataStore.getStock().put(productId, new Integer(current.intValue() + quantity));
    }

    public void decreaseStock(String productId, int quantity) {
        Integer current = (Integer) dataStore.getStock().get(productId);

        if (current == null) {
            current = new Integer(0);
        }

        int newValue = current.intValue() - quantity;

        if (newValue < 0) {
            throw new IllegalArgumentException("Negative stock");
        }

        dataStore.getStock().put(productId, new Integer(newValue));
    }

    public int getStock(String productId) {
        Integer value = (Integer) dataStore.getStock().get(productId);

        if (value == null) {
            return 0;
        }

        return value.intValue();
    }
}