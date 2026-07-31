package com.store.inventory;

import com.store.finance.CashflowEntry;
import com.store.finance.CashflowService;
import com.store.finance.CashflowType;
import com.store.supplier.SupplierService;

import java.util.ArrayList;
import java.util.List;

public class RestockService {
    private StockService stockService;
    private SupplierService supplierService;
    private CashflowService cashflowService;
    private List<RestockOperation> operations = new ArrayList<RestockOperation>();

    public RestockService(StockService stockService, SupplierService supplierService, CashflowService cashflowService) {
        this.stockService = stockService;
        this.supplierService = supplierService;
        this.cashflowService = cashflowService;
    }

    public RestockOperation registerRestock(String id, String productId, String supplierId, int quantity, double unitCost) {
        if (supplierService.getSupplier(supplierId) == null) {
            throw new RuntimeException("Supplier not found.");
        }
        RestockOperation operation = new RestockOperation(id, productId, supplierId, quantity, unitCost);
        operations.add(operation);

        stockService.increase(productId, quantity);

        CashflowEntry entry = new CashflowEntry("RST_" + id, CashflowType.OUTFLOW, operation.getTotalCost(), "Restock operation " + id);
        cashflowService.recordEntry(entry);

        return operation;
    }

    public List<RestockOperation> getAllOperations() {
        return new ArrayList<RestockOperation>(operations);
    }
}