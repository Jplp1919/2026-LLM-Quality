package com.store.service;

import java.util.HashMap;
import java.util.Map;
import com.store.domain.Customer;
import com.store.domain.Supplier;
import com.store.exception.ValidationException;

public class CustomerService {
    private Map<String, Customer> customers;
    private Map<String, Supplier> suppliers;

    public CustomerService() {
        this.customers = new HashMap<String, Customer>();
        this.suppliers = new HashMap<String, Supplier>();
    }

    public void registerCustomer(Customer customer) {
        if (customers.containsKey(customer.getId())) {
            throw new ValidationException("Duplicate customer identifier");
        }
        customers.put(customer.getId(), customer);
    }

    public Customer getCustomer(String id) {
        return customers.get(id);
    }

    public void registerSupplier(Supplier supplier) {
        if (suppliers.containsKey(supplier.getId())) {
            throw new ValidationException("Duplicate supplier identifier");
        }
        suppliers.put(supplier.getId(), supplier);
    }

    public Supplier getSupplier(String id) {
        return suppliers.get(id);
    }

    public int getCustomerCount() {
        return customers.size();
    }

    public int getSupplierCount() {
        return suppliers.size();
    }
}