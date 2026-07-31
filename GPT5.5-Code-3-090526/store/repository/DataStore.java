package store.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import store.model.cashflow.CashflowEntry;
import store.model.category.Category;
import store.model.customer.Customer;
import store.model.order.Order;
import store.model.product.Product;
import store.model.restock.RestockOperation;
import store.model.returning.ReturnRequest;
import store.model.supplier.Supplier;

public class DataStore {
    private Map products;
    private Map customers;
    private Map suppliers;
    private Map stock;
    private List categories;
    private List orders;
    private List returns;
    private List restocks;
    private List cashflows;

    public DataStore() {
        products = new HashMap();
        customers = new HashMap();
        suppliers = new HashMap();
        stock = new HashMap();
        categories = new ArrayList();
        orders = new ArrayList();
        returns = new ArrayList();
        restocks = new ArrayList();
        cashflows = new ArrayList();
    }

    public void addCategory(Category category) {
        categories.add(category);
    }

    public void addProduct(Product product) {
        if (products.containsKey(product.getId())) {
            throw new IllegalArgumentException("Duplicate product");
        }

        products.put(product.getId(), product);
    }

    public void addCustomer(Customer customer) {
        if (customers.containsKey(customer.getId())) {
            throw new IllegalArgumentException("Duplicate customer");
        }

        customers.put(customer.getId(), customer);
    }

    public void addSupplier(Supplier supplier) {
        if (suppliers.containsKey(supplier.getId())) {
            throw new IllegalArgumentException("Duplicate supplier");
        }

        suppliers.put(supplier.getId(), supplier);
    }

    public Product getProduct(String id) {
        return (Product) products.get(id);
    }

    public Customer getCustomer(String id) {
        return (Customer) customers.get(id);
    }

    public Supplier getSupplier(String id) {
        return (Supplier) suppliers.get(id);
    }

    public int getProductCount() {
        return products.size();
    }

    public int getCustomerCount() {
        return customers.size();
    }

    public int getSupplierCount() {
        return suppliers.size();
    }

    public void setStock(String productId, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Negative stock");
        }

        stock.put(productId, new Integer(quantity));
    }

    public int getStock(String productId) {
        Integer value = (Integer) stock.get(productId);

        if (value == null) {
            return 0;
        }

        return value.intValue();
    }

    public void increaseStock(String productId, int quantity) {
        setStock(productId, getStock(productId) + quantity);
    }

    public void decreaseStock(String productId, int quantity) {
        int current = getStock(productId);

        if (current - quantity < 0) {
            throw new IllegalArgumentException("Negative stock");
        }

        setStock(productId, current - quantity);
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public List getOrders() {
        return orders;
    }

    public void addReturn(ReturnRequest request) {
        returns.add(request);
    }

    public List getReturns() {
        return returns;
    }

    public void addRestock(RestockOperation operation) {
        restocks.add(operation);
    }

    public List getRestocks() {
        return restocks;
    }

    public void addCashflow(CashflowEntry entry) {
        cashflows.add(entry);
    }

    public List getCashflows() {
        return cashflows;
    }

    public Map getProductsMap() {
        return products;
    }
}