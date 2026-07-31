package com.store.domain;

public class CashflowEntry {
private String id;
private double amount;
private CashflowType type;
private String description;

public CashflowEntry(String id, double amount, CashflowType type, String description) {
    this.id = id;
    this.amount = amount;
    this.type = type;
    this.description = description;
}

public String getId() {
    return id;
}

public double getAmount() {
    return amount;
}

public CashflowType getType() {
    return type;
}

public String getDescription() {
    return description;
}
}