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
import store.model.returns.ReturnRequest;
import store.model.supplier.Supplier;

public class DataStore {

    private Map<String, Category> categories;
    private Map<String, Product> products;
    private Map<String, Integer> stock;
    private Map<String, Customer> customers;
    private Map<String, Supplier> suppliers;
    private List<Order> orders;
    private List<ReturnRequest> returns;
    private List<RestockOperation> restocks;
    private List<CashflowEntry> cashflows;

    public DataStore() {
        categories = new HashMap<String, Category>();
        products = new HashMap<String, Product>();
        stock = new HashMap<String, Integer>();
        customers = new HashMap<String, Customer>();
        suppliers = new HashMap<String, Supplier>();
        orders = new ArrayList<Order>();
        returns = new ArrayList<ReturnRequest>();
        restocks = new ArrayList<RestockOperation>();
        cashflows = new ArrayList<CashflowEntry>();
    }

    public Map<String, Category> getCategories() {
        return categories;
    }

    public Map<String, Product> getProducts() {
        return products;
    }

    public Map<String, Integer> getStock() {
        return stock;
    }

    public Map<String, Customer> getCustomers() {
        return customers;
    }

    public Map<String, Supplier> getSuppliers() {
        return suppliers;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public List<ReturnRequest> getReturns() {
        return returns;
    }

    public List<RestockOperation> getRestocks() {
        return restocks;
    }

    public List<CashflowEntry> getCashflows() {
        return cashflows;
    }
}