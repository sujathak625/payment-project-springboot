package com.cgi.fsdc.serviceImpl;

import com.cgi.fsdc.repository.TransactionRepository;
import com.cgi.fsdc.service.CustomerService;
import com.cgi.fsdc.service.Impl.TransactionServiceImpl;
import com.cgi.fsdc.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CustomerService customerService;

    private final TransactionService transactionService;

    public TransactionServiceImplTest() {
        MockitoAnnotations.openMocks(this);
        transactionService = new TransactionServiceImpl(transactionRepository, customerService);
    }

    @Test
    void testBlockCard_Success() {
        int customerId = 1001;
        when(customerService.blockCard(eq(customerId))).thenReturn(true);
        boolean result = transactionService.blockCard(customerId, true);
        assertTrue(result);
        verify(customerService, times(1)).blockCard(eq(customerId));
    }

    @Test
    void testBlockCard_Failure() {
        int customerId = 1001;
        when(customerService.blockCard(eq(customerId))).thenReturn(false);
        boolean result = transactionService.blockCard(customerId, true);
        assertFalse(result);
        verify(customerService, times(1)).blockCard(eq(customerId));
    }
}
