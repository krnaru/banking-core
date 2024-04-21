package org.example.corebanking.domain.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.corebanking.domain.enums.TransactionDirection;

import java.math.BigDecimal;

@Data
public class TransactionRequest {
    @NotNull(message = "Account ID cannot be null")
    private Long accountId;
    @NotNull(message = "Amount cannot be null")
    private BigDecimal amount;
    @NotNull(message = "Currency cannot be null")
    private String currency;
    @NotNull(message = "Direction cannot be null")
    private TransactionDirection direction;
    @NotNull(message = "Description cannot be null")
    private String description;
}
