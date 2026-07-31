package store.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import store.model.cashflow.CashflowEntry;
import store.model.catalog.Category;
import store.model.catalog.Product;
import store.model.customer.Customer;
import store.model.order.Order;
import store.model.promotion.Promotion;
import store.model.restock.RestockOperation;
import store.model.returns.ReturnRequest;
import store.model.supplier.Supplier;

public class DataStore {

    private Map products;
    private Map customers;
    private Map suppliers;
    private Map stock;
    private List categories;
    private List promotions;
    private List orders;
    private List returns;
    private List restocks;
    private List cashflowEntries;

    public DataStore() {
        products = new HashMap();
        customers = new HashMap();
        suppliers = new HashMap();
        stock = new HashMap();
        categories = new ArrayList();
        promotions = new ArrayList();
        orders = new ArrayList();
        returns = new ArrayList();
        restocks = new ArrayList();
        cashflowEntries = new ArrayList();
    }

    public Map getProducts() {
        return products;
    }

    public Map getCustomers() {
        return customers;
    }

    public Map getSuppliers() {
        return suppliers;
    }

    public Map getStock() {
        return stock;
    }

    public List getCategories() {
        return categories;
    }

    public List getPromotions() {
        return promotions;
    }

    public List getOrders() {
        return orders;
    }

    public List getReturns() {
        return returns;
    }

    public List getRestocks() {
        return restocks;
    }

    public List getCashflowEntries() {
        return cashflowEntries;
    }
}