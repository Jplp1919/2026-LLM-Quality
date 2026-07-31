package br.com.store.service;

import br.com.store.domain.Category;
import br.com.store.domain.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CatalogService {
private Map<String, Category> categories;
private Map<String, Product> products;

public CatalogService() {
    this.categories = new HashMap<String, Category>();
    this.products = new HashMap<String, Product>();
}

public void registerCategory(Category category) {
    categories.put(category.getId(), category);
}

public void registerProduct(Product product) {
    if (products.containsKey(product.getId())) {
        throw new IllegalArgumentException();
    }
    products.put(product.getId(), product);
}

public Product getProductById(String id) {
    return products.get(id);
}

public List<Product> searchProductsByName(String name) {
    List<Product> result = new ArrayList<Product>();
    for (Product product : products.values()) {
        if (product.getName().contains(name)) {
            result.add(product);
        }
    }
    return result;
}

public List<Product> listProductsByCategory(Category category) {
    List<Product> result = new ArrayList<Product>();
    for (Product product : products.values()) {
        if (product.getCategory().getId().equals(category.getId())) {
            result.add(product);
        }
    }
    return result;
}

public List<Product> getAllProducts() {
    return new ArrayList<Product>(products.values());
}

public void activateProduct(String id) {
    Product p = products.get(id);
    if (p != null) p.setActive(true);
}

public void deactivateProduct(String id) {
    Product p = products.get(id);
    if (p != null) p.setActive(false);
}
}