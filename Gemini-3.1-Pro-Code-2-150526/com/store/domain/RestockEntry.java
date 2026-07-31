package com.store.domain;

import com.store.exception.ValidationException;

public class RestockEntry {
    private String id;
    private Product product;
    private Supplier supplier;
    private int quantity;
    private double unitCost;

    public RestockEntry(String id, Product product, Supplier supplier, int quantity, double unitCost) {
        if (quantity <= 0) {
            throw new ValidationException("Restock quantity must be positive");
        }
        if (unitCost < 0) {
            throw new ValidationException("Restock unit cost cannot be negative");
        }
        this.id = id;
        this.product = product;
        this.supplier = supplier;
        this.quantity = quantity;
        this.unitCost = unitCost;
    }

    public String getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public double getTotalCost() {
        return quantity * unitCost;
    }
}