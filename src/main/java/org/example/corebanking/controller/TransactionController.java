package org.example.corebanking.controller;

import jakarta.validation.Valid;
import org.example.corebanking.domain.request.TransactionRequest;
import org.example.corebanking.domain.response.Transaction;
import org.example.corebanking.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This is the controller for handling transaction-related requests.
 * It uses the TransactionService to process the business logic related to transactions.
 */
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Endpoint for creating a new transaction.
     * @param transactionRequest The request body containing the details of the transaction to be created.
     * @return The created transaction.
     */
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody @Valid TransactionRequest transactionRequest) {
        return ResponseEntity.ok(transactionService.createTransaction(transactionRequest));
    }

    /**
     * Endpoint for retrieving transactions for a specific account.
     * @param accountId The ID of the account for which to retrieve transactions.
     * @return A list of transactions for the specified account.
     */
    @GetMapping("/{accountId}")
    public ResponseEntity<List<Transaction>> getTransaction(@PathVariable Long accountId) {
        List<Transaction> transactions = transactionService.getTransaction(accountId);
        if (transactions == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transactions);
    }
}