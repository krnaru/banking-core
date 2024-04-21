package org.example.corebanking.service;

import lombok.extern.slf4j.Slf4j;
import org.example.corebanking.domain.response.Account;
import org.example.corebanking.domain.request.AccountRequest;
import org.example.corebanking.domain.response.Balance;
import org.example.corebanking.repository.AccountMapper;
import org.example.corebanking.repository.BalanceMapper;
import org.example.corebanking.service.messagingQueue.RabbitMQSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AccountService {

    private final RabbitMQSender rabbitMQSender;
    private final AccountMapper accountMapper;
    private final BalanceMapper balanceMapper;

    public AccountService(AccountMapper accountMapper, BalanceMapper balanceMapper, RabbitMQSender rabbitMQSender) {
        this.accountMapper = accountMapper;
        this.balanceMapper = balanceMapper;
        this.rabbitMQSender = rabbitMQSender;
    }

    @Transactional
    public Account createAccount(AccountRequest accountRequest) {
        log.info("Creating account with customer ID: " + accountRequest.getCustomerId());
        if (accountRequest.getCustomerId() == null) {
            throw new IllegalArgumentException("Customer ID cannot be null");
        }
        Account account = new Account();
        account.setCustomerId(accountRequest.getCustomerId());
        account.setCountry(accountRequest.getCountry());

        accountMapper.insertAccount(account);

        List<Balance> balances = accountRequest.getCurrencies().stream()
                .map(currency -> createBalance(account.getId(), currency))
                .collect(Collectors.toList());
        account.setBalances(balances);
        balances.forEach(balanceMapper::insertBalance);
        rabbitMQSender.sendAccountMessage("Account created: " + account);
        return account;
    }

    public Account getAccount(Long id) {
        Account account = accountMapper.getAccount(id);
        if (account != null) {
            List<Balance> balances = balanceMapper.getBalancesByAccountId(account.getId());
            account.setBalances(balances);
        }
        else {
            throw new IllegalArgumentException("Account with ID: " + id + " not found");
        }
        return account;
    }

    private Balance createBalance(Long accountId, String currency) {
        Balance balance = new Balance();
        balance.setAccountId(accountId);
        balance.setCurrency(currency);
        balance.setAvailableAmount(BigDecimal.ZERO);
        return balance;
    }
}