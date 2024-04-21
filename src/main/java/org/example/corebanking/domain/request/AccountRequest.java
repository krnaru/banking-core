package org.example.corebanking.domain.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class AccountRequest {
    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;
    @NotNull(message = "country cannot be null")
    private String country;
    @NotEmpty(message = "Currencies cannot be empty")
    private List<String> currencies;
}
