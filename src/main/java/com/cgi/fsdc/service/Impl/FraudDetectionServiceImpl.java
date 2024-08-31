package com.cgi.fsdc.service.Impl;

import com.cgi.fsdc.entity.Transaction;
import com.cgi.fsdc.repository.TransactionRepository;
import com.cgi.fsdc.service.FraudDetectionService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class FraudDetectionServiceImpl implements FraudDetectionService {

    private final TransactionRepository transactionRepository;

    public FraudDetectionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public boolean detectFraud(Integer customerId) {
        Instant twoHoursAgo = Instant.now().minus(2, ChronoUnit.HOURS);
        List<Transaction> transactions = transactionRepository.findByCustomerIdAndCreateTimeAfter(customerId, twoHoursAgo);

        return transactions.size() >= 50;
    }
}
