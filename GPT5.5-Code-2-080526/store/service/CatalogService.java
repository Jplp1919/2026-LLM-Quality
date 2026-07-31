package store.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import store.model.category.Category;
import store.model.product.Product;
import store.repository.DataStore;

public class CatalogService {

    private DataStore dataStore;

    public CatalogService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public void addCategory(Category category) {
        dataStore.getCategories().put(category.getId(), category);
    }

    public void addProduct(Product product) {
        Map<String, Product> products = dataStore.getProducts();

        if (products.containsKey(product.getId())) {
            throw new IllegalArgumentException("Duplicate product");
        }

        products.put(product.getId(), product);
    }

    public Product findProductById(String id) {
        return (Product) dataStore.getProducts().get(id);
    }

    public List<Product> findProductByName(String name) {
        List<Product> result = new ArrayList<Product>();
        List<Product> all = new ArrayList<Product>(dataStore.getProducts().values());

        int i;

        for (i = 0; i < all.size(); i++) {
            Product product = (Product) all.get(i);

            if (product.getName().toLowerCase().indexOf(name.toLowerCase()) >= 0) {
                result.add(product);
            }
        }

        return result;
    }

    public List<Product> listByCategory(String categoryId) {
        List<Product> result = new ArrayList<Product>();
        List<Product> all = new ArrayList<Product>(dataStore.getProducts().values());

        int i;

        for (i = 0; i < all.size(); i++) {
            Product product = (Product) all.get(i);

            if (product.getCategory().getId().equals(categoryId)) {
                result.add(product);
            }
        }

        return result;
    }
}