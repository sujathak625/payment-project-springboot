package com.cgi.fsdc.serviceImpl;

import com.cgi.fsdc.entity.Customer;
import com.cgi.fsdc.model.response.DeviceAuthResponse;
import com.cgi.fsdc.repository.CustomerRepository;
import com.cgi.fsdc.service.DeviceService;
import com.cgi.fsdc.service.Impl.DeviceServiceImpl;
import com.cgi.fsdc.utilities.enums.YesNo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class DeviceServiceImplTest {

    private DeviceService deviceService;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        deviceService = new DeviceServiceImpl(customerRepository);
    }

    @Test
    void testDeviceAuthentication_Success() {
        int customerId = 10001;
        String deviceId = "DEVICE123";
        Customer customer = new Customer();
        customer.setCustId(customerId);
        customer.setDeviceId(deviceId);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        DeviceAuthResponse response = deviceService.deviceAuthentication(customerId, deviceId);
        assertEquals(YesNo.YES, response.getStatus());
        assertEquals("Device authorized", response.getMessage());
    }

    @Test
    void testDeviceAuthentication_Failure_DeviceNotFound() {
        int customerId = 10001;
        String deviceId = "DEVICE123";
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        DeviceAuthResponse response = deviceService.deviceAuthentication(customerId, deviceId);
        assertEquals(YesNo.NO, response.getStatus());
        assertEquals("Device not authorized", response.getMessage());
    }

    @Test
    void testDeviceAuthentication_Failure_DeviceMismatch() {
        int customerId = 10001;
        String deviceId = "DIFFERENT_DEVICE";
        Customer customer = new Customer();
        customer.setCustId(customerId);
        customer.setDeviceId("DEVICE123");
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        DeviceAuthResponse response = deviceService.deviceAuthentication(customerId, deviceId);
        assertEquals(YesNo.NO, response.getStatus());
        assertEquals("Device not authorized", response.getMessage());
    }
}