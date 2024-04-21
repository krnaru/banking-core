package org.example.corebanking.controller;

import jakarta.validation.Valid;
import org.example.corebanking.domain.enums.Currency;
import org.example.corebanking.domain.response.Account;
import org.example.corebanking.domain.request.AccountRequest;
import org.example.corebanking.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing accounts.
 * It uses the Spring's @RestController annotation to indicate that it's a REST controller.
 * It also uses the @RequestMapping annotation to map the URL path for this controller.
 */
@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Endpoint for creating an account.
     * It uses the @PostMapping annotation to map the HTTP POST method for this endpoint.
     * @param account the account request
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody @Valid AccountRequest account) {
        try {
            validateCurrencies(account.getCurrencies());
            Account responseAccount = accountService.createAccount(account);
            return ResponseEntity.ok(responseAccount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint for getting an account by ID.
     * It uses the @GetMapping annotation to map the HTTP GET method for this endpoint.
     * @param id the account ID
     * @return the response entity
     */
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable Long id) {
        Account account = accountService.getAccount(id);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(account);
    }


    /**
     * Method for validating currencies.
     * @param currencies the list of currencies
     * @throws IllegalArgumentException if any currency is invalid
     */
    private void validateCurrencies(List<String> currencies) throws IllegalArgumentException {
        List<String> invalidCurrencies = currencies.stream()
                .filter(currency -> !Currency.isValidCurrency(currency))
                .collect(Collectors.toList());

        if (!invalidCurrencies.isEmpty()) {
            throw new IllegalArgumentException("Invalid currency/currencies: " + String.join(", ", invalidCurrencies));
        }
    }}
