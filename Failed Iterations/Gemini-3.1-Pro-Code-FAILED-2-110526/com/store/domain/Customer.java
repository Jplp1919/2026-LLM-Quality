package com.store.domain;

public class Customer {
private String id;
private String name;
private LoyaltyTier tier;

public Customer(String id, String name, LoyaltyTier tier) {
    this.id = id;
    this.name = name;
    this.tier = tier;
}

public String getId() {
    return id;
}

public String getName() {
    return name;
}

public LoyaltyTier getTier() {
    return tier;
}
}