package com.cgi.fsdc.controller;

import com.cgi.fsdc.entity.Customer;
import com.cgi.fsdc.model.request.DeviceAuthRequest;
import com.cgi.fsdc.model.response.DeviceAuthResponse;
import com.cgi.fsdc.model.request.TransactionRequest;
import com.cgi.fsdc.service.CustomerService;
import com.cgi.fsdc.service.TransactionService;
import com.cgi.fsdc.service.FraudDetectionService;
import com.cgi.fsdc.utilities.enums.YesNo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

	private final TransactionService transactionService;
	private final FraudDetectionService fraudDetectionService;
	private final CustomerService customerService;
	private final MessageSource messageSource;
	private final RestTemplate restTemplate;

	@Value("${device-auth.url}")
	private String deviceAuthBaseUrl;

	public TransactionController(TransactionService transactionService,
								 FraudDetectionService fraudDetectionService,
								 CustomerService customerService,
								 MessageSource messageSource,
								 RestTemplate restTemplate) {
		this.transactionService = transactionService;
		this.fraudDetectionService = fraudDetectionService;
		this.customerService = customerService;
		this.messageSource = messageSource;
		this.restTemplate = restTemplate;
	}

	@PostMapping("/create")
	public ResponseEntity<String> createTransaction(@Valid @RequestBody TransactionRequest transactionRequest) {
		String deviceAuthUrl = String.format("%s/authorizeCustomer", deviceAuthBaseUrl);

		DeviceAuthRequest deviceAuthRequest = new DeviceAuthRequest(
				transactionRequest.getCustomerId(),
				transactionRequest.getDeviceId()
		);
		DeviceAuthResponse deviceAuthResponse = restTemplate.postForObject(deviceAuthUrl, deviceAuthRequest, DeviceAuthResponse.class);
		if (deviceAuthResponse != null && deviceAuthResponse.getStatus() == YesNo.YES) {
			transactionService.saveTransactionDetails(transactionRequest);
			String successMessage = messageSource.getMessage("success.transaction.created", null, Locale.getDefault());
			return ResponseEntity.ok(successMessage);
		} else {
			String errorMessage = messageSource.getMessage("failed.transaction.authorization", null, Locale.getDefault());
			return ResponseEntity.status(403).body(errorMessage);
		}
	}

	@GetMapping("/fraud-detection/{customerId}")
	public ResponseEntity<String> detectFraud(@PathVariable Integer customerId) {
		Optional<Customer> customer = Optional.ofNullable(customerService.findCustomerById(customerId));
		if (customer.isEmpty()) {
			String errorMessage = messageSource.getMessage("customerId.notFound", new Object[]{customerId}, Locale.getDefault());
			return ResponseEntity.status(404).body(errorMessage);
		}
		boolean isFraudulent = fraudDetectionService.detectFraud(customerId);
		boolean isCardBlocked = transactionService.blockCard(customerId, isFraudulent);
		if (isFraudulent && isCardBlocked) {
			String fraudMessage = messageSource.getMessage("fraud.detection.fraud", new Object[]{String.valueOf(customerId)}, Locale.getDefault());
			return ResponseEntity.status(403).body(fraudMessage);
		} else {
			String noFraudMessage = messageSource.getMessage("fraud.detection.noFraud", new Object[]{String.valueOf(customerId)}, Locale.getDefault());
			return ResponseEntity.ok(noFraudMessage);
		}
	}
}

