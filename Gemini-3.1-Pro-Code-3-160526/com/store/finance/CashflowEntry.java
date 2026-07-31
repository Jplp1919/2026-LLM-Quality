package com.store.finance;

import com.store.exception.StoreBusinessException;

public class CashflowEntry {
    private String id;
    private CashflowType type;
    private double amount;
    private String description;

    public CashflowEntry(String id, CashflowType type, double amount, String description) {
        if (amount < 0) {
            throw new StoreBusinessException("Cashflow amount cannot be negative.");
        }
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public CashflowType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}