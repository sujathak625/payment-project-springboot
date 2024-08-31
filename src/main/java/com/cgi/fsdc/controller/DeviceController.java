package com.cgi.fsdc.controller;

import com.cgi.fsdc.model.request.DeviceAuthRequest;
import com.cgi.fsdc.model.response.DeviceAuthResponse;
import com.cgi.fsdc.service.DeviceService;
import com.cgi.fsdc.utilities.enums.YesNo;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/device-auth")
@Validated
public class DeviceController {

	private final DeviceService deviceService;

	public DeviceController(DeviceService deviceService) {
		this.deviceService = deviceService;
	}

	@PostMapping("/authorizeCustomer")
	public ResponseEntity<DeviceAuthResponse> detectDevice(@Valid @RequestBody DeviceAuthRequest request) {
		DeviceAuthResponse response = deviceService.deviceAuthentication(request.getCustomerId(), request.getDeviceId());
		return response.getStatus() == YesNo.YES
				? ResponseEntity.ok(response)
				: ResponseEntity.status(403).body(response);
	}
}
