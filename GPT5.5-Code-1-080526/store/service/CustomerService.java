package store.service;

import store.model.customer.Customer;
import store.repository.DataStore;

public class CustomerService {

    private DataStore dataStore;

    public CustomerService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public void registerCustomer(Customer customer) {
        if (dataStore.getCustomers().containsKey(customer.getId())) {
            throw new IllegalArgumentException("Duplicate customer");
        }

        dataStore.getCustomers().put(customer.getId(), customer);
    }
}