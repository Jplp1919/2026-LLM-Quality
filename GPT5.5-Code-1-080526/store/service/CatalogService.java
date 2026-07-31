package store.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import store.model.catalog.Category;
import store.model.catalog.Product;
import store.repository.DataStore;

public class CatalogService {

    private DataStore dataStore;

    public CatalogService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public void addCategory(Category category) {
        dataStore.getCategories().add(category);
    }

    public void addProduct(Product product) {
        if (dataStore.getProducts().containsKey(product.getId())) {
            throw new IllegalArgumentException("Duplicate product");
        }

        dataStore.getProducts().put(product.getId(), product);
    }

    public Product findById(String id) {
        return (Product) dataStore.getProducts().get(id);
    }

    public List findByName(String name) {
        List result = new ArrayList();

        Iterator iterator = dataStore.getProducts().values().iterator();

        while (iterator.hasNext()) {
            Product product = (Product) iterator.next();

            if (product.getName().toLowerCase().indexOf(name.toLowerCase()) >= 0) {
                result.add(product);
            }
        }

        return result;
    }

    public List listByCategory(String categoryId) {
        List result = new ArrayList();

        Iterator iterator = dataStore.getProducts().values().iterator();

        while (iterator.hasNext()) {
            Product product = (Product) iterator.next();

            if (product.getCategory().getId().equals(categoryId)) {
                result.add(product);
            }
        }

        return result;
    }
}