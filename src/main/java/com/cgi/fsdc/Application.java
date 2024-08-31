package com.cgi.fsdc;

import com.cgi.fsdc.entity.Customer;
import com.cgi.fsdc.entity.Transaction;
import com.cgi.fsdc.repository.CustomerRepository;
import com.cgi.fsdc.repository.TransactionRepository;
import com.cgi.fsdc.utilities.SecurityUtils;
import com.cgi.fsdc.utilities.enums.CardStatus;
import com.cgi.fsdc.utilities.enums.Currency;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	// Test Data for API Testing
	@Bean
	CommandLineRunner initCustomerTransactionData(CustomerRepository customerRepository, TransactionRepository transactionRepository) {
		return args -> {
			// 10 customer Records for testing device authorization
			Customer sita = customerRepository.save(new Customer(100002, "Sita", "Mumbai, India", "DEVICE5678", "sita@gmail.com", CardStatus.ACTIVE));
			Customer amit = customerRepository.save(new Customer(100001, "Amit", "Delhi, India", "DEVICE1234", "amit@gmail.com", CardStatus.ACTIVE));
			Customer hans = customerRepository.save(new Customer(100003, "Hans", "Berlin, Germany", "DEVICE9101", "hans@gmail.com", CardStatus.ACTIVE));
			Customer greta = customerRepository.save(new Customer(100004, "Greta", "Munich, Germany", "DEVICE1121", "greta@gmail.com", CardStatus.ACTIVE));
			Customer john = customerRepository.save(new Customer(100005, "John", "New York, USA", "DEVICE3141", "john@gmail.com", CardStatus.ACTIVE));
			Customer emily = customerRepository.save(new Customer(100006, "Emily", "Los Angeles, USA", "DEVICE5161", "emily@gmail.com", CardStatus.ACTIVE));
			Customer wei = customerRepository.save(new Customer(100007, "Wei", "Beijing, China", "DEVICE7181", "wei@gmail.com", CardStatus.ACTIVE));
			Customer li = customerRepository.save(new Customer(100008, "Li", "Shanghai, China", "DEVICE9201", "li@gmail.com", CardStatus.ACTIVE));
			Customer vladimir = customerRepository.save(new Customer(100009, "Vladimir", "Moscow, Russia", "DEVICE1222", "vladimir@gmail.com", CardStatus.ACTIVE));
			Customer olga = customerRepository.save(new Customer(100010, "Olga", "Saint Petersburg, Russia", "DEVICE3242", "olga@gmail.com", CardStatus.ACTIVE));

			Map<Integer, String> customerCardMap = new HashMap<>();

			customerCardMap.put(sita.getCustId(), SecurityUtils.encrypt(generateCardNumber()));
			customerCardMap.put(amit.getCustId(), SecurityUtils.encrypt(generateCardNumber()));
			customerCardMap.put(hans.getCustId(), SecurityUtils.encrypt(generateCardNumber()));
			customerCardMap.put(greta.getCustId(), SecurityUtils.encrypt(generateCardNumber()));
			customerCardMap.put(john.getCustId(), SecurityUtils.encrypt(generateCardNumber()));
			customerCardMap.put(emily.getCustId(), SecurityUtils.encrypt(generateCardNumber()));
			customerCardMap.put(wei.getCustId(), SecurityUtils.encrypt(generateCardNumber()));
			customerCardMap.put(li.getCustId(), SecurityUtils.encrypt(generateCardNumber()));
			customerCardMap.put(vladimir.getCustId(), SecurityUtils.encrypt(generateCardNumber()));
			customerCardMap.put(olga.getCustId(), SecurityUtils.encrypt(generateCardNumber()));

			Random random = new Random();
			Instant now = Instant.now();

			// Adding 55 transactions for Sita in last two years for case fraud trasnactions detected
			for (int i = 0; i < 55; i++) {
				Instant transactionTime = now.minus(i * 2, ChronoUnit.MINUTES);
				transactionRepository.save(Transaction.builder()
						.customerId(sita.getCustId())
						.amount(BigDecimal.valueOf(1 + random.nextInt(3000)))
						.currency(Currency.EUR)
						.deviceId(sita.getDeviceId())
						.status("SUCCESS")
						.cardNumber(customerCardMap.get(sita.getCustId()))
						.expiryDate(LocalDate.of(2025, 12, 31))
						.createTime(transactionTime)
						.build());
			}

			// Adding 52 transactions for Amit with varying dates over the last year. Those are not considered as fraud trasnactions
			for (int i = 0; i < 52; i++) {
				Instant transactionTime = now.minus(i * 7, ChronoUnit.DAYS);
				transactionRepository.save(Transaction.builder()
						.customerId(amit.getCustId())
						.amount(BigDecimal.valueOf(1 + random.nextInt(3000)))
						.currency(Currency.EUR)
						.deviceId(amit.getDeviceId())
						.status("SUCCESS")
						.cardNumber(customerCardMap.get(amit.getCustId()))
						.expiryDate(LocalDate.of(2028, 12, 31))
						.createTime(transactionTime)
						.build());
			}

			Customer[] otherCustomers = {hans, greta, john, emily, wei, li, vladimir, olga};
			for (Customer customer : otherCustomers) {
				transactionRepository.save(Transaction.builder()
						.customerId(customer.getCustId())
						.amount(BigDecimal.valueOf(1 + random.nextInt(3000)))
						.currency(Currency.EUR)
						.deviceId(customer.getDeviceId())
						.status("SUCCESS")
						.cardNumber(customerCardMap.get(customer.getCustId()))
						.expiryDate(LocalDate.of(2028, 12, 31))
						.createTime(Instant.now())
						.build());
			}
		};
	}


	private String generateCardNumber() {
		Random random = new Random();
		StringBuilder sb = new StringBuilder(16);
		for (int i = 0; i < 16; i++) {
			sb.append(random.nextInt(10));
		}
		return sb.toString();
	}
}
