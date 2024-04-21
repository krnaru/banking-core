package org.example.corebanking.integration;

import org.example.corebanking.domain.request.AccountRequest;
import org.example.corebanking.domain.response.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AccountIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Sql(scripts = {"/accounts/insert-account-data.sql"})
    @Sql(scripts = {"/common/remove-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testCreateAccountSuccess() {
        AccountRequest request = new AccountRequest();
        request.setCustomerId(1L);
        request.setCountry("USA");
        request.setCurrencies(Arrays.asList("USD", "EUR"));

        ResponseEntity<Account> response = restTemplate.postForEntity("/accounts", request, Account.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(Long.valueOf(1), response.getBody().getCustomerId());
        assertEquals(2, response.getBody().getBalances().size());
        assertEquals("EUR", response.getBody().getBalances().get(1).getCurrency());
        assertEquals("USD", response.getBody().getBalances().get(0).getCurrency());

    }

    @Test
    public void testCreateAccountMissingCustomerId() {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setCountry("USA");
        accountRequest.setCurrencies(Arrays.asList("USD", "EUR"));

        ResponseEntity<String> response = restTemplate.postForEntity("/accounts", accountRequest, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).contains("Customer ID cannot be null"));
    }

    @Test
    public void testCreateAccountEmptyCurrencies() {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setCustomerId(1L);
        accountRequest.setCountry("USA");
        accountRequest.setCurrencies(new ArrayList<>());

        ResponseEntity<String> response = restTemplate.postForEntity("/accounts", accountRequest, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).contains("Currencies cannot be empty"));
    }

    @Test
    @Sql(scripts = {"/accounts/insert-account-data.sql"})
    @Sql(scripts = {"/common/remove-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetAccount() {
        ResponseEntity<String> response = restTemplate.getForEntity("/accounts/{id}", String.class, 1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"id\":1,\"customerId\":1001,\"country\":\"USA\",\"balances\":[{\"id\":1," +
                "\"accountId\":1,\"availableAmount\":1000.0000,\"currency\":\"USD\"}]}", response.getBody());
    }

    @Test
    @Sql(scripts = {"/accounts/insert-account-data.sql"})
    @Sql(scripts = {"/common/remove-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetAccountNoAccountFound() {
        ResponseEntity<String> response = restTemplate.getForEntity("/accounts/{id}", String.class, 4);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}