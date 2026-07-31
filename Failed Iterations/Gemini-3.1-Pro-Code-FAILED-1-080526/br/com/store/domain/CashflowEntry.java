package br.com.store.domain;

public class CashflowEntry {
private String id;
private CashflowType type;
private double amount;
private String description;

public CashflowEntry(String id, CashflowType type, double amount, String description) {
    if (amount < 0) {
        throw new IllegalArgumentException();
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