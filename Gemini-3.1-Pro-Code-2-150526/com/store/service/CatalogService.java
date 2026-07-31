package com.store.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.store.domain.Category;
import com.store.domain.Product;
import com.store.exception.ValidationException;

public class CatalogService {
    private Map<String, Category> categories;
    private Map<String, Product> products;

    public CatalogService() {
        this.categories = new HashMap<String, Category>();
        this.products = new HashMap<String, Product>();
    }

    public void addCategory(Category category) {
        categories.put(category.getId(), category);
    }

    public Category getCategory(String id) {
        return categories.get(id);
    }

    public void addProduct(Product product) {
        if (products.containsKey(product.getId())) {
            throw new ValidationException("Duplicate product identifier");
        }
        products.put(product.getId(), product);
    }

    public Product getProduct(String id) {
        return products.get(id);
    }

    public List<Product> searchProductsByName(String name) {
        List<Product> result = new ArrayList<Product>();
        for (Product p : products.values()) {
            if (p.getName().toLowerCase().indexOf(name.toLowerCase()) != -1) {
                result.add(p);
            }
        }
        return result;
    }

    public List<Product> listProductsByCategory(String categoryId) {
        List<Product> result = new ArrayList<Product>();
        for (Product p : products.values()) {
            if (p.getCategory().getId().equals(categoryId)) {
                result.add(p);
            }
        }
        return result;
    }

    public void activateProduct(String id) {
        Product p = getProduct(id);
        if (p != null) {
            p.setActive(true);
        }
    }

    public void deactivateProduct(String id) {
        Product p = getProduct(id);
        if (p != null) {
            p.setActive(false);
        }
    }

    public int getProductCount() {
        return products.size();
    }
}