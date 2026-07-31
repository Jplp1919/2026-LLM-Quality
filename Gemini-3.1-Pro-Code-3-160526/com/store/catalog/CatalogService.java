package com.store.catalog;

import com.store.exception.StoreBusinessException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CatalogService {
    private Map<String, Category> categories = new HashMap<String, Category>();
    private Map<String, Product> products = new HashMap<String, Product>();

    public void registerCategory(Category category) {
        categories.put(category.getId(), category);
    }

    public void registerProduct(Product product) {
        if (products.containsKey(product.getId())) {
            throw new StoreBusinessException("Duplicate product identifier.");
        }
        products.put(product.getId(), product);
    }

    public Product getProduct(String id) {
        return products.get(id);
    }

    public List<Product> searchByName(String name) {
        List<Product> result = new ArrayList<Product>();
        for (Product p : products.values()) {
            if (p.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(p);
            }
        }
        return result;
    }

    public List<Product> listByCategory(String categoryId) {
        List<Product> result = new ArrayList<Product>();
        for (Product p : products.values()) {
            if (p.getCategory() != null && p.getCategory().getId().equals(categoryId)) {
                result.add(p);
            }
        }
        return result;
    }

    public void setProductActive(String id, boolean active) {
        Product p = getProduct(id);
        if (p != null) {
            p.setActive(active);
        }
    }

    public int getProductCount() {
        return products.size();
    }
}