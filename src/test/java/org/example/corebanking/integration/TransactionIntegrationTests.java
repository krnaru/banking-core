package org.example.corebanking.integration;

import org.example.corebanking.domain.enums.TransactionDirection;
import org.example.corebanking.domain.request.TransactionRequest;
import org.example.corebanking.domain.response.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TransactionIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Sql(scripts = {"/transactions/insert-transactions-data.sql"})
    @Sql(scripts = {"/common/remove-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testCreateTransactionSuccess() {
        TransactionRequest request = new TransactionRequest();
        request.setAccountId(1L);
        request.setAmount(new BigDecimal("100.00"));
        request.setCurrency("USD");
        request.setDirection(TransactionDirection.IN);
        request.setDescription("Deposit into savings");

        ResponseEntity<Transaction> response = restTemplate.postForEntity("/transactions", request, Transaction.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(Long.valueOf(1), response.getBody().getAccountId());
        assertEquals(new BigDecimal("100.00"), response.getBody().getAmount());
        assertEquals("USD", response.getBody().getCurrency());
        assertEquals(TransactionDirection.IN, response.getBody().getDirection());
        assertNotNull(response.getBody().getBalanceAfterTransaction());
    }

    @Test
    public void testCreateTransactionMissingAccountId() {
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAmount(new BigDecimal("50.00"));
        transactionRequest.setCurrency("USD");
        transactionRequest.setDirection(TransactionDirection.OUT);
        transactionRequest.setDescription("Withdrawal from checking");

        ResponseEntity<String> response = restTemplate.postForEntity("/transactions", transactionRequest, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).contains("Account ID cannot be null"));
    }

    @Test
    @Sql(scripts = {"/transactions/insert-transactions-data.sql"})
    @Sql(scripts = {"/common/remove-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetTransaction() {
        ResponseEntity<String> response = restTemplate.getForEntity("/transactions/{id}", String.class, 1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("[{\"id\":1,\"accountId\":1,\"amount\":100.0000,\"currency\":\"EUR\",\"direction\":\"IN\"," +
                "\"description\":\"sissemakse\",\"balanceAfterTransaction\":null}]", response.getBody());
    }

    @Test
    @Sql(scripts = {"/transactions/insert-transactions-data.sql"})
    @Sql(scripts = {"/common/remove-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetTransactionsNotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity("/transactions/{id}", String.class, 10);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}