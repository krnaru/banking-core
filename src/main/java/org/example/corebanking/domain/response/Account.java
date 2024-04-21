package org.example.corebanking.domain.response;

import lombok.Data;
import java.util.List;

@Data
public class Account {
    private Long id;
    private Long customerId;
    private String country;
    private List<Balance> balances;
}
