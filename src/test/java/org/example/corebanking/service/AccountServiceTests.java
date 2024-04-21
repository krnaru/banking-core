package org.example.corebanking.service;

import org.example.corebanking.domain.request.AccountRequest;
import org.example.corebanking.domain.response.Account;
import org.example.corebanking.repository.AccountMapper;
import org.example.corebanking.repository.BalanceMapper;
import org.example.corebanking.service.messagingQueue.RabbitMQSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountServiceTests {

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private BalanceMapper balanceMapper;

    @Mock
    private RabbitMQSender rabbitMQSender;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createAccount_withValidRequest_createsAccount() {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setCustomerId(1L);
        accountRequest.setCountry("US");
        accountRequest.setCurrencies(Arrays.asList("USD", "EUR"));

        Account account = accountService.createAccount(accountRequest);

        assertNotNull(account);
        assertEquals(1L, account.getCustomerId());
        assertEquals("US", account.getCountry());
        assertEquals(2, account.getBalances().size());
        verify(accountMapper, times(1)).insertAccount(any(Account.class));
        verify(balanceMapper, times(2)).insertBalance(any());
        verify(rabbitMQSender, times(1)).sendAccountMessage(anyString());
    }

    @Test
    public void createAccount_withNullCustomerId_throwsException() {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setCustomerId(null);
        accountRequest.setCountry("US");
        accountRequest.setCurrencies(Arrays.asList("USD", "EUR"));

        assertThrows(IllegalArgumentException.class, () -> accountService.createAccount(accountRequest));
    }

    @Test
    public void getAccount_withExistingId_returnsAccount() {
        Account mockAccount = new Account();
        mockAccount.setId(1L);
        when(accountMapper.getAccount(1L)).thenReturn(mockAccount);

        Account account = accountService.getAccount(1L);

        assertNotNull(account);
        verify(accountMapper, times(1)).getAccount(1L);
        verify(balanceMapper, times(1)).getBalancesByAccountId(anyLong());
    }

    @Test
    public void getAccount_withNonExistingId_throwsException() {
        when(accountMapper.getAccount(1L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> accountService.getAccount(1L));
    }
}