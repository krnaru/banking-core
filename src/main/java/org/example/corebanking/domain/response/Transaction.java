package org.example.corebanking.domain.response;

import lombok.Data;
import org.example.corebanking.domain.enums.TransactionDirection;

import java.math.BigDecimal;

@Data
public class Transaction {
    private Long id;
    private Long accountId;
    private BigDecimal amount;
    private String currency;
    private TransactionDirection direction;
    private String description;
    private BigDecimal balanceAfterTransaction;
}
