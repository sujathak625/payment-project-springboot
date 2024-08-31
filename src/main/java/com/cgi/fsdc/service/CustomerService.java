package com.cgi.fsdc.service;

import com.cgi.fsdc.entity.Customer;

public interface CustomerService {
    Customer findCustomerById(int customerId);
    boolean blockCard(int customerId);
    void saveCustomer(Customer customer);
}
