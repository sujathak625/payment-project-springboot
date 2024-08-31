package com.cgi.fsdc.service.Impl;

import com.cgi.fsdc.entity.Customer;
import com.cgi.fsdc.repository.CustomerRepository;
import com.cgi.fsdc.service.CustomerService;
import com.cgi.fsdc.utilities.enums.CardStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer findCustomerById(int customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        return customer.orElse(null);
    }

    @Override
    public boolean blockCard(int customerId) {
        int updatedRows = customerRepository.updateCardStatusByCustomerId(customerId, CardStatus.BLOCKED);
        return updatedRows > 0;
    }

    @Override
    public void saveCustomer(Customer customer) {
        customerRepository.saveAndFlush(customer);
    }
}
