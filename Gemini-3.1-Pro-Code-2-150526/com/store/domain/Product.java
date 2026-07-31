package com.store.domain;

import com.store.exception.ValidationException;

public class Product {
    private String id;
    private String name;
    private double unitPrice;
    private Category category;
    private boolean active;

    public Product(String id, String name, double unitPrice, Category category) {
        if (id == null || id.trim().length() == 0) {
            throw new ValidationException("Product ID cannot be empty");
        }
        if (unitPrice < 0) {
            throw new ValidationException("Unit price cannot be negative");
        }
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.category = category;
        this.active = true;
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

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        if (unitPrice < 0) {
            throw new ValidationException("Unit price cannot be negative");
        }
        this.unitPrice = unitPrice;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}