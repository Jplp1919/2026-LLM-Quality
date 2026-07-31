package com.store.service;

import java.util.ArrayList;
import java.util.List;
import com.store.domain.CashflowType;
import com.store.domain.Product;
import com.store.domain.RestockOperation;
import com.store.domain.Supplier;

public class RestockService {
private InventoryService inventoryService;
private CashflowService cashflowService;
private List restockOperations = new ArrayList();

public RestockService(InventoryService inventoryService, CashflowService cashflowService) {
    this.inventoryService = inventoryService;
    this.cashflowService = cashflowService;
}

public RestockOperation registerRestock(String restockId, Product product, Supplier supplier, int quantity, double unitCost) {
    RestockOperation operation = new RestockOperation(restockId, product, supplier, quantity, unitCost);
    inventoryService.increaseStock(product.getId(), quantity);
    cashflowService.recordEntry("RST" + restockId, operation.getTotalCost(), CashflowType.OUTFLOW, "Restock operation " + restockId);
    restockOperations.add(operation);
    return operation;
}

public List<RestockOperation> getAllRestockOperations() {
    return new ArrayList<RestockOperation>(restockOperations);
}
}