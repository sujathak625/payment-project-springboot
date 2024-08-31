package com.cgi.fsdc.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceAuthRequest {
    private Integer customerId;
    private String deviceId;
}
