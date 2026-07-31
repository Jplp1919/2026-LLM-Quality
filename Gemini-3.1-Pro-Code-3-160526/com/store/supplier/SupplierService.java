package com.store.supplier;

import com.store.exception.StoreBusinessException;
import java.util.HashMap;
import java.util.Map;

public class SupplierService {
    private Map<String, Supplier> suppliers = new HashMap<String, Supplier>();

    public void registerSupplier(Supplier supplier) {
        if (suppliers.containsKey(supplier.getId())) {
            throw new StoreBusinessException("Duplicate supplier identifier.");
        }
        suppliers.put(supplier.getId(), supplier);
    }

    public Supplier getSupplier(String id) {
        return suppliers.get(id);
    }

    public int getSupplierCount() {
        return suppliers.size();
    }
}