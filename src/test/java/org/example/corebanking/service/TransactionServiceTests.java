package org.example.corebanking.service;

import org.example.corebanking.domain.enums.TransactionDirection;
import org.example.corebanking.domain.request.TransactionRequest;
import org.example.corebanking.domain.response.Balance;
import org.example.corebanking.domain.response.Transaction;
import org.example.corebanking.repository.BalanceMapper;
import org.example.corebanking.repository.TransactionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceTests {

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private BalanceMapper balanceMapper;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createTransaction_withValidRequest_createsTransaction() {
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAccountId(1L);
        transactionRequest.setAmount(new BigDecimal("100.00"));
        transactionRequest.setCurrency("USD");
        transactionRequest.setDirection(TransactionDirection.IN);
        transactionRequest.setDescription("Test transaction");

        Balance balance = new Balance();
        balance.setId(1L);
        balance.setAccountId(1L);
        balance.setCurrency("USD");
        balance.setAvailableAmount(new BigDecimal("1000.00"));

        when(balanceMapper.getBalanceByAccountIdAndCurrency(1L, "USD")).thenReturn(balance);

        Transaction transaction = transactionService.createTransaction(transactionRequest);

        assertNotNull(transaction);
        assertEquals(1L, transaction.getAccountId());
        assertEquals(new BigDecimal("100.00"), transaction.getAmount());
        assertEquals("USD", transaction.getCurrency());
        assertEquals(TransactionDirection.IN, transaction.getDirection());
        assertEquals("Test transaction", transaction.getDescription());
        verify(transactionMapper, times(1)).insertTransaction(any(Transaction.class));
        verify(balanceMapper, times(1)).updateBalance(anyLong(), any(BigDecimal.class));
    }

    @Test
    public void createTransaction_withInsufficientFunds_throwsException() {
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAccountId(1L);
        transactionRequest.setAmount(new BigDecimal("2000.00"));
        transactionRequest.setCurrency("USD");
        transactionRequest.setDirection(TransactionDirection.OUT);
        transactionRequest.setDescription("Test transaction");

        Balance balance = new Balance();
        balance.setId(1L);
        balance.setAccountId(1L);
        balance.setCurrency("USD");
        balance.setAvailableAmount(new BigDecimal("1000.00"));

        when(balanceMapper.getBalanceByAccountIdAndCurrency(1L, "USD")).thenReturn(balance);

        assertThrows(IllegalArgumentException.class, () -> transactionService.createTransaction(transactionRequest));
    }

    @Test
    public void getTransaction_withExistingId_returnsTransaction() {
        List<Transaction> expectedTransactions = Arrays.asList(new Transaction(), new Transaction());
        when(transactionMapper.getTransactionsByAccountId(1L)).thenReturn(expectedTransactions);

        List<Transaction> actualTransactions = transactionService.getTransaction(1L);

        assertEquals(expectedTransactions, actualTransactions);
        verify(transactionMapper, times(1)).getTransactionsByAccountId(1L);
    }

    @Test
    public void getTransaction_withNonExistingId_throwsException() {
        when(transactionMapper.getTransactionsByAccountId(1L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> transactionService.getTransaction(1L));
    }
}