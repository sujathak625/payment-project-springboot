package com.cgi.fsdc.service.Impl;

import com.cgi.fsdc.entity.Customer;
import com.cgi.fsdc.model.response.DeviceAuthResponse;
import com.cgi.fsdc.repository.CustomerRepository;
import com.cgi.fsdc.utilities.enums.YesNo;
import org.springframework.stereotype.Service;

import com.cgi.fsdc.service.DeviceService;

import java.util.Optional;

@Service
public class DeviceServiceImpl implements DeviceService {

	private final CustomerRepository customerRepository;

	public DeviceServiceImpl(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	public DeviceAuthResponse deviceAuthentication(int customerId, String deviceId) {
		Optional<Customer> customer = customerRepository.findById(customerId);
		if (customer.isPresent() && customer.get().getDeviceId().equals(deviceId)) {
			return new DeviceAuthResponse(YesNo.YES, "Device authorized");
		}
		return new DeviceAuthResponse(YesNo.NO, "Device not authorized");
	}
}
