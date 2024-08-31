package com.cgi.fsdc.controller;

import com.cgi.fsdc.model.request.DeviceAuthRequest;
import com.cgi.fsdc.model.response.DeviceAuthResponse;
import com.cgi.fsdc.service.DeviceService;
import com.cgi.fsdc.utilities.enums.YesNo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class DeviceControllerTest {

    @Mock
    private DeviceService deviceService;

    private DeviceController deviceController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        deviceController = new DeviceController(deviceService);
    }

    @Test
    void testDetectDevice_Authorized() {
        DeviceAuthRequest request = new DeviceAuthRequest();
        request.setCustomerId(1);
        request.setDeviceId("device123");
        DeviceAuthResponse response = new DeviceAuthResponse();
        response.setStatus(YesNo.YES);
        when(deviceService.deviceAuthentication(1,"device123")).thenReturn(response);
        ResponseEntity<DeviceAuthResponse> responseEntity = deviceController.detectDevice(request);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(response, responseEntity.getBody());
    }

    @Test
    void testDetectDevice_NotAuthorized() {
        DeviceAuthRequest request = new DeviceAuthRequest();
        request.setCustomerId(1);
        request.setDeviceId("device123");
        DeviceAuthResponse response = new DeviceAuthResponse();
        response.setStatus(YesNo.NO);
        when(deviceService.deviceAuthentication(anyInt(), anyString())).thenReturn(response);
        ResponseEntity<DeviceAuthResponse> responseEntity = deviceController.detectDevice(request);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertEquals(response, responseEntity.getBody());
    }
}
