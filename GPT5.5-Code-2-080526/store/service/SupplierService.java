package store.service;

import store.model.supplier.Supplier;
import store.repository.DataStore;

public class SupplierService {

    private DataStore dataStore;

    public SupplierService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public void registerSupplier(Supplier supplier) {
        if (dataStore.getSuppliers().containsKey(supplier.getId())) {
            throw new IllegalArgumentException("Duplicate supplier");
        }

        dataStore.getSuppliers().put(supplier.getId(), supplier);
    }
}