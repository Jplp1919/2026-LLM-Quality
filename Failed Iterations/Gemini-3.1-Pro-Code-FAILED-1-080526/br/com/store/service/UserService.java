package br.com.store.service;

import br.com.store.domain.Customer;
import br.com.store.domain.Supplier;

import java.util.HashMap;
import java.util.Map;

public class UserService {
private Map<String, Customer> customers;
private Map<String, Supplier> suppliers;

public UserService() {
    this.customers = new HashMap<String, Customer>();
    this.suppliers = new HashMap<String, Supplier>();
}

public void registerCustomer(Customer customer) {
    if (customers.containsKey(customer.getId())) {
        throw new IllegalArgumentException();
    }
    customers.put(customer.getId(), customer);
}

public void registerSupplier(Supplier supplier) {
    if (suppliers.containsKey(supplier.getId())) {
        throw new IllegalArgumentException();
    }
    suppliers.put(supplier.getId(), supplier);
}

public int getCustomerCount() {
    return customers.size();
}

public int getSupplierCount() {
    return suppliers.size();
}
}