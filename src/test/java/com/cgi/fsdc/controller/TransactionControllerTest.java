package com.cgi.fsdc.controller;

import com.cgi.fsdc.entity.Customer;
import com.cgi.fsdc.model.request.DeviceAuthRequest;
import com.cgi.fsdc.model.request.TransactionRequest;
import com.cgi.fsdc.model.response.DeviceAuthResponse;
import com.cgi.fsdc.service.CustomerService;
import com.cgi.fsdc.service.TransactionService;
import com.cgi.fsdc.service.FraudDetectionService;
import com.cgi.fsdc.utilities.enums.YesNo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;

class TransactionControllerTest {
    @Mock
    private TransactionService transactionService;
    @Mock
    private FraudDetectionService fraudDetectionService;
    @Mock
    private CustomerService customerService;
    @Mock
    private MessageSource messageSource;
    @Mock
    private RestTemplate restTemplate;
    @Autowired
    private MockMvc mockMvc;

    private TransactionController transactionController;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionController = new TransactionController(transactionService, fraudDetectionService, customerService, messageSource, restTemplate);
        this.mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    void testCreateTransaction_Success() {
        TransactionRequest request = new TransactionRequest();
        request.setCustomerId(1);
        request.setDeviceId("device123");
        DeviceAuthResponse deviceAuthResponse = new DeviceAuthResponse();
        deviceAuthResponse.setStatus(YesNo.YES);
        deviceAuthResponse.setMessage(("Device authorized"));
        when(restTemplate.postForObject(anyString(), any(DeviceAuthRequest.class), eq(DeviceAuthResponse.class)))
                .thenReturn(deviceAuthResponse);
        when(messageSource.getMessage(eq("success.transaction.created"), any(), any(Locale.class)))
                .thenReturn("Transaction created successfully");
        ResponseEntity<String> response = transactionController.createTransaction(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Transaction created successfully", response.getBody());
        verify(transactionService, times(1)).saveTransactionDetails(any(TransactionRequest.class));
    }

    @Test
    void testCreateTransaction_Failure() {
        TransactionRequest request = new TransactionRequest();
        request.setCustomerId(1);
        request.setDeviceId("device123");
        DeviceAuthResponse deviceAuthResponse = new DeviceAuthResponse();
        deviceAuthResponse.setStatus(YesNo.NO);
        when(restTemplate.postForObject(anyString(), any(DeviceAuthRequest.class), eq(DeviceAuthResponse.class)))
                .thenReturn(deviceAuthResponse);
        when(messageSource.getMessage(eq("failed.transaction.authorization"), any(), any(Locale.class)))
                .thenReturn("Transaction authorization failed");
        ResponseEntity<String> response = transactionController.createTransaction(request);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Transaction authorization failed", response.getBody());
        verify(transactionService, never()).saveTransactionDetails(any(TransactionRequest.class));
    }

    @Test
    void testDetectFraud_FraudDetected() {
        int customerId = 1;
        Customer customer = new Customer();
        customer.setCustId(customerId);
        when(customerService.findCustomerById(anyInt())).thenReturn(customer);
        when(fraudDetectionService.detectFraud(customerId)).thenReturn(true);
        when(transactionService.blockCard(customerId, true)).thenReturn(true);
        when(messageSource.getMessage(eq("fraud.detection.fraud"), any(), any(Locale.class)))
                .thenReturn("Fraud detected for customer 1");
        ResponseEntity<String> response = transactionController.detectFraud(customerId);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Fraud detected for customer 1", response.getBody());
    }

    @Test
    void testDetectFraud_NoFraudDetected() {
        int customerId = 1;
        Customer customer = new Customer();
        customer.setCustId(customerId);
        when(customerService.findCustomerById(anyInt())).thenReturn(customer);
        when(fraudDetectionService.detectFraud(customerId)).thenReturn(false);
        when(transactionService.blockCard(customerId, false)).thenReturn(false);
        when(messageSource.getMessage(eq("fraud.detection.noFraud"), any(), any(Locale.class)))
                .thenReturn("No fraud detected for customer 1");
        ResponseEntity<String> response = transactionController.detectFraud(customerId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("No fraud detected for customer 1", response.getBody());
    }

    @Test
    void testDetectFraud_CustomerNotFound() {
        int customerId = 1;
        when(customerService.findCustomerById(anyInt())).thenReturn(null);
        when(messageSource.getMessage(eq("customerId.notFound"), any(), any(Locale.class)))
                .thenReturn("Customer with ID 1 not found");
        ResponseEntity<String> response = transactionController.detectFraud(customerId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Customer with ID 1 not found", response.getBody());
    }
}
