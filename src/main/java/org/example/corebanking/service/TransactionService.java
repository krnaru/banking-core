package org.example.corebanking.service;

import org.apache.commons.lang3.EnumUtils;
import org.example.corebanking.domain.enums.TransactionDirection;
import org.example.corebanking.domain.request.TransactionRequest;
import org.example.corebanking.domain.response.Balance;
import org.example.corebanking.domain.response.Transaction;
import org.example.corebanking.repository.BalanceMapper;
import org.example.corebanking.repository.TransactionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionMapper transactionMapper;

    private final BalanceMapper balanceMapper;

    public TransactionService(TransactionMapper transactionMapper, BalanceMapper balanceMapper) {
        this.balanceMapper = balanceMapper;
        this.transactionMapper = transactionMapper;
    }

    @Transactional
    public Transaction createTransaction(TransactionRequest transactionRequest) {
        validateTransactionRequest(transactionRequest);

        Balance balance = balanceMapper.getBalanceByAccountIdAndCurrency(transactionRequest.getAccountId(), transactionRequest.getCurrency());
        if (balance == null) {
            throw new IllegalArgumentException("No balance available for the given account and currency.");
        }

        BigDecimal updatedBalance = calculateUpdatedBalance(balance, transactionRequest.getAmount(), transactionRequest.getDirection());

        Transaction newTransaction = new Transaction();
        newTransaction.setAccountId(transactionRequest.getAccountId());
        newTransaction.setAmount(transactionRequest.getAmount());
        newTransaction.setCurrency(transactionRequest.getCurrency());
        newTransaction.setDirection(transactionRequest.getDirection());
        newTransaction.setDescription(transactionRequest.getDescription());
        newTransaction.setBalanceAfterTransaction(updatedBalance);

        transactionMapper.insertTransaction(newTransaction);
        balanceMapper.updateBalance(balance.getId(), updatedBalance);

        return newTransaction;
    }

    private void validateTransactionRequest(TransactionRequest request) {
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }
        if (!EnumUtils.isValidEnum(TransactionDirection.class, request.getDirection().name())) {
            throw new IllegalArgumentException("Invalid direction");
        }
    }

    private BigDecimal calculateUpdatedBalance(Balance balance, BigDecimal amount, TransactionDirection direction) {
        return switch (direction) {
            case IN -> balance.getAvailableAmount().add(amount);
            case OUT -> {
                if (balance.getAvailableAmount().compareTo(amount) < 0) {
                    throw new IllegalArgumentException("Insufficient funds");
                }
                yield balance.getAvailableAmount().subtract(amount);
            }
        };
    }

    public List<Transaction> getTransaction(Long accountId) {
        List<Transaction> transactions = transactionMapper.getTransactionsByAccountId(accountId);
        if (transactions == null || transactions.isEmpty()) {
            throw new IllegalArgumentException("No transactions found for the given account");
        }
        return transactions;
    }

}