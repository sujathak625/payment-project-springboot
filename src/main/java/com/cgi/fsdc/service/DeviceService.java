package com.cgi.fsdc.service;

import com.cgi.fsdc.model.response.DeviceAuthResponse;

public interface DeviceService {

	DeviceAuthResponse deviceAuthentication(int customerId, String deviceId);
}



