package com.cgi.fsdc.service;

import com.cgi.fsdc.model.request.TransactionRequest;

public interface TransactionService {

	boolean saveTransactionDetails(TransactionRequest transactionRequest);
	boolean blockCard(int customerId, boolean isFraudSuspected);
}
