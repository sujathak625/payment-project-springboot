package com.cgi.fsdc.service.Impl;

import com.cgi.fsdc.entity.Customer;
import com.cgi.fsdc.entity.Transaction;
import com.cgi.fsdc.model.request.TransactionRequest;
import com.cgi.fsdc.repository.TransactionRepository;
import com.cgi.fsdc.service.CustomerService;
import com.cgi.fsdc.service.TransactionService;
import com.cgi.fsdc.utilities.SecurityUtils;
import com.cgi.fsdc.utilities.enums.CardStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final CustomerService customerService;

    public TransactionServiceImpl(TransactionRepository transactionRepository, CustomerService customerService) {
        this.transactionRepository = transactionRepository;
        this.customerService = customerService;
    }

    @Override
    @Transactional
    public boolean saveTransactionDetails(TransactionRequest transactionRequest) {
        Customer customer = customerService.findCustomerById(transactionRequest.getCustomerId());
        CardStatus cardStatus = customer.getStatus();
        if (cardStatus != CardStatus.ACTIVE) {
            return false;
        }
        String encryptedCardNumber = SecurityUtils.encrypt(transactionRequest.getCardNumber());
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setCurrency(transactionRequest.getCurrency());
        transaction.setCustomerId(transactionRequest.getCustomerId());
        transaction.setDeviceId(transactionRequest.getDeviceId());
        transaction.setStatus(transactionRequest.getStatus());
        transaction.setCardNumber(encryptedCardNumber);
        transaction.setExpiryDate(transactionRequest.getExpiryDate());
        transaction.setCreateTime(Instant.now());

        transactionRepository.save(transaction);
        return true;
    }

    @Override
    public boolean blockCard(int customerId, boolean isFraudSuspected) {
        boolean isBlocked = false;
        if (isFraudSuspected) {
            isBlocked = customerService.blockCard(customerId);
        }
        return isBlocked;
    }


}
