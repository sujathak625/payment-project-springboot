package com.cgi.fsdc.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cgi.fsdc.utilities.enums.Currency;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "Transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "transaction_id")
	private Integer transactionId;

	@Column(name = "amount")
	private BigDecimal amount;

	@Enumerated(EnumType.STRING)
	private Currency currency;

	@Column(name = "create_time")
	private Instant createTime;

	@Column(name = "cust_id")
	private Integer customerId;

	@Column(name = "device_id")
	private String deviceId;

	@Column(name = "status")
	private String status;

	@Column(name = "card_number")
	private String cardNumber;

	@Column(name = "expiry_date")
	private LocalDate expiryDate;
}
