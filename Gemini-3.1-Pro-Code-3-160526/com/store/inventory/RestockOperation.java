package com.store.inventory;

import com.store.exception.StoreBusinessException;

public class RestockOperation {
    private String id;
    private String productId;
    private String supplierId;
    private int quantity;
    private double unitCost;
    private double totalCost;

    public RestockOperation(String id, String productId, String supplierId, int quantity, double unitCost) {
        if (quantity <= 0) {
            throw new StoreBusinessException("Restock quantities must be positive.");
        }
        if (unitCost < 0) {
            throw new StoreBusinessException("Restock unit costs cannot be negative.");
        }
        this.id = id;
        this.productId = productId;
        this.supplierId = supplierId;
        this.quantity = quantity;
        this.unitCost = unitCost;
        this.totalCost = quantity * unitCost;
    }

    public String getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public double getTotalCost() {
        return totalCost;
    }
}