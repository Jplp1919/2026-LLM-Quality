package com.store.service;

import java.util.HashMap;
import java.util.Map;
import com.store.domain.Customer;
import com.store.exception.StoreBusinessException;

public class CustomerService {
private Map<String, Customer> customers = new HashMap<String, Customer>();

public void registerCustomer(Customer customer) {
    if (customers.containsKey(customer.getId())) {
        throw new StoreBusinessException("Duplicate customer identifier");
    }
    customers.put(customer.getId(), customer);
}

public Customer getCustomer(String id) {
    return customers.get(id);
}

public int getCustomerCount() {
    return customers.size();
}
}