package com.store.domain;

import com.store.exception.ValidationException;

public class Category {
    private String id;
    private String name;

    public Category(String id, String name) {
        if (id == null || id.trim().length() == 0) {
            throw new ValidationException("Category ID cannot be empty");
        }
        this.id = id;
        this.name = name;
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
}