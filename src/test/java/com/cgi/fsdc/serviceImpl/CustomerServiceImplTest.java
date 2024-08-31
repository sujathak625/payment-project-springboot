package com.cgi.fsdc.serviceImpl;

import com.cgi.fsdc.entity.Customer;
import com.cgi.fsdc.repository.CustomerRepository;
import com.cgi.fsdc.service.Impl.CustomerServiceImpl;
import com.cgi.fsdc.utilities.enums.CardStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    private CustomerServiceImpl customerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        customerService = new CustomerServiceImpl(customerRepository);
    }

    @Test
    void testBlockCard_Success() {
        int customerId = 1001;
        when(customerRepository.updateCardStatusByCustomerId(eq(customerId), eq(CardStatus.BLOCKED)))
                .thenReturn(1);
        boolean result = customerService.blockCard(customerId);
        assertTrue(result);
    }

    @Test
    void testBlockCard_Failure() {
        int customerId = 1001;
        when(customerRepository.updateCardStatusByCustomerId(eq(customerId), eq(CardStatus.BLOCKED)))
                .thenReturn(0);
        boolean result = customerService.blockCard(customerId);
        assertFalse(result);
    }

    @Test
    void testFindCustomerById_Success() {
        int customerId = 1001;
        Customer customer = new Customer();
        customer.setCustId(customerId);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        Customer result = customerService.findCustomerById(customerId);
        assertEquals(customerId, result.getCustId());
    }

    @Test
    void testFindCustomerById_NotFound() {
        int customerId = 1001;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        Customer result = customerService.findCustomerById(customerId);
        assertEquals(null, result);
    }

    @Test
    void testSaveCustomer() {
        Customer customer = new Customer();
        customer.setCustId(1001);
        when(customerRepository.saveAndFlush(any(Customer.class))).thenReturn(customer);
        customerService.saveCustomer(customer);
        assertEquals(1001, customer.getCustId());
    }
}
