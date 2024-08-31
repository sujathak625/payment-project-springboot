package com.cgi.fsdc.entity;

import javax.persistence.*;

import com.cgi.fsdc.utilities.enums.CardStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "Customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
	@Id
	@Column(name = "cust_id")
	private Integer custId;

	@Column(name = "customer_name")
	private String customerName;

	@Column(name = "address")
	private String address;

	@Column(name = "device_id")
	private String deviceId;

	@Column(name = "email_id")
	private String emailId;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private CardStatus status;
}
