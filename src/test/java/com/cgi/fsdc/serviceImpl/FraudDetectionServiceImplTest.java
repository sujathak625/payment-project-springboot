package com.cgi.fsdc.serviceImpl;

import com.cgi.fsdc.entity.Transaction;
import com.cgi.fsdc.repository.TransactionRepository;
import com.cgi.fsdc.service.FraudDetectionService;
import com.cgi.fsdc.service.Impl.FraudDetectionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class FraudDetectionServiceImplTest {

    private FraudDetectionService fraudDetectionService;

    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fraudDetectionService = new FraudDetectionServiceImpl(transactionRepository);
    }

    @Test
    void testDetectFraud_True() {
        Integer customerId = 10001;
        Instant now = Instant.now();

        // Create 51 transactions
        List<Transaction> transactions = new ArrayList<>();
        for (int i = 0; i < 51; i++) {
            Transaction transaction = new Transaction();
            transaction.setCustomerId(customerId);
            transaction.setDeviceId("DEVICE" + (i % 5));
            transaction.setCreateTime(now.minusSeconds(3600));
            transactions.add(transaction);
        }

        when(transactionRepository.findByCustomerIdAndCreateTimeAfter(eq(customerId), any(Instant.class)))
                .thenReturn(transactions);

        boolean result = fraudDetectionService.detectFraud(customerId);
        assertTrue(result);
    }


    @Test
    void testDetectFraud_False_NoFraudDetected() {
        Integer customerId = 10001;
        Instant now = Instant.now();
        Transaction transaction = new Transaction();
        transaction.setCustomerId(customerId);
        transaction.setDeviceId("DEVICE1");
        transaction.setCreateTime(now.minusSeconds(3600));
        List<Transaction> transactions = List.of(transaction);
        when(transactionRepository.findByCustomerIdAndCreateTimeAfter(customerId, now.minusSeconds(7200)))
                .thenReturn(transactions);

        boolean result = fraudDetectionService.detectFraud(customerId);
        assertFalse(result);
    }
}