package com.cgi.fsdc.model.request;

import com.cgi.fsdc.utilities.enums.Currency;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionRequest {

    private Integer transactionId;

    @NotNull(message = "{amount.notNull}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{amount.positive}")
    @Digits(integer = 10, fraction = 2, message = "{amount.format}")
    private BigDecimal amount;

    private Currency currency;

    @NotNull(message = "{customerId.notNull}")
    @Min(value = 1, message = "{customerId.min}")
    private Integer customerId;

    @NotEmpty(message = "{deviceId.notEmpty}")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "{deviceId.pattern}")
    private String deviceId;

    private String status;

    @NotNull(message = "{cardNumber.notNull}")
    @Pattern(regexp = "^[0-9]{16}$", message = "{cardNumber.invalid}")
    private String cardNumber;

    @NotNull(message = "{expiryDate.notNull}")
    private LocalDate expiryDate;
}
