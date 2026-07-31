package com.store.domain;

import com.store.domain.enums.CustomerTier;
import com.store.exception.ValidationException;

public class Customer {
    private String id;
    private String name;
    private CustomerTier tier;

    public Customer(String id, String name, CustomerTier tier) {
        if (id == null || id.trim().length() == 0) {
            throw new ValidationException("Customer ID cannot be empty");
        }
        this.id = id;
        this.name = name;
        this.tier = tier;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CustomerTier getTier() {
        return tier;
    }

    public void setTier(CustomerTier tier) {
        this.tier = tier;
    }
}