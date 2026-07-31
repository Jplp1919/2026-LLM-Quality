package com.store.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.store.domain.Category;
import com.store.domain.Product;
import com.store.exception.StoreBusinessException;

public class CatalogService {
private Map<String, Category> categories = new HashMap<String, Category>();
private Map<String, Product> products = new HashMap<String, Product>();

public void registerCategory(Category category) {
    categories.put(category.getId(), category);
}

public void registerProduct(Product product) {
    if (products.containsKey(product.getId())) {
        throw new StoreBusinessException("Duplicate product identifier");
    }
    products.put(product.getId(), product);
}

public Product searchProductById(String id) {
    return products.get(id);
}

public List<Product> searchProductsByName(String name) {
    List<Product> result = new ArrayList<Product>();
    for (Product p : products.values()) {
        if (p.getName().toLowerCase().contains(name.toLowerCase())) {
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
    Product p = products.get(id);
    if (p != null) {
        p.setActive(true);
    }
}

public void deactivateProduct(String id) {
    Product p = products.get(id);
    if (p != null) {
        p.setActive(false);
    }
}

public int getProductCount() {
    return products.size();
}
}