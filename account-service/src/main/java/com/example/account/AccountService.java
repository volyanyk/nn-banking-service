package com.example.account;


import com.example.account.domain.AccountDomain;
import com.example.account.domain.AccountDomainService;
import com.example.account.model.*;
import com.example.api.exception.ResourceNotFoundException;
import com.example.common.domain.valueobject.AccountId;
import com.example.common.domain.valueobject.CustomerId;
import com.example.mq.client.account.AccountRequest;
import com.example.mq.client.account.AccountType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountDomainService accountDomainService;

    public BalanceResponse createAccount(Integer customerId, OpenAccountRequest request) {
        AccountDomain accountDomain = accountDomainService.create(
                new CustomerId(customerId),
                AccountType.valueOf(request.currency()),
                request.initialBalance());
        return new BalanceResponse(
                Collections.singletonList(
                new Balance(accountDomain.getId().getValue(),
                        accountDomain.getBalance(),
                        accountDomain.getType().name())));
    }

    public BalanceResponse transfer(Integer customerId, TransferRequest transferRequest) {
        List<AccountDomain> accountDomains = accountDomainService.transfer(
                new CustomerId(customerId),
                transferRequest.fromId(),
                transferRequest.toId(),
                transferRequest.volume());
        return new BalanceResponse(accountDomains.stream()
                .map(account ->
                        new Balance(account.getId().getValue(), account.getBalance(), account.getType().name())).toList());
    }

    public BalanceResponse getBalanceByCurrency(Integer customerId, String currency) {
        final Optional<Account> byCustomerIdAndType = accountRepository.findByCustomerIdAndType(customerId, AccountType.valueOf(currency));
        return byCustomerIdAndType.map(account ->
                new BalanceResponse(
                        Collections.singletonList(
                                new Balance(account.getId(), account.getBalance(), account.getType().name())))).orElseThrow(() ->
                        new ResourceNotFoundException("The account for the customer id "
                                + customerId +
                                " and currency "
                                + currency +
                                " was not found."));
    }

    public BalanceResponse getBalance(Integer customerId) {
        final List<Account> byCustomerId = accountRepository.findByCustomerId(customerId);
        return new BalanceResponse(byCustomerId.stream()
                .map(account ->
                        new Balance(account.getId(), account.getBalance(), account.getType().name())).toList());
    }

    public BalanceResponse getBalanceById(Integer customerId, Integer accountId) {
        final Optional<Account> byId = accountRepository.findByIdAndCustomerId(accountId, customerId);
        return byId.map(account ->
                new BalanceResponse(
                        Collections.singletonList(
                                new Balance(account.getId(), account.getBalance(), account.getType().name())))).orElseThrow(() ->
                new ResourceNotFoundException("The account for the id "
                        + byId +
                        " was not found."));
    }

    public BalanceResponse getBalanceByCurrencyAndId(Integer customerId, String currency, Integer id) {
        final Optional<Account> byId = accountRepository.findByIdAndCustomerIdAndType(id, customerId, AccountType.valueOf(currency));
        return byId.map(account ->
                new BalanceResponse(
                        Collections.singletonList(
                                new Balance(account.getId(), account.getBalance(), account.getType().name())))).orElseThrow(() ->
                new ResourceNotFoundException("The account for the id "
                        + byId +
                        " was not found."));
    }

    public void createInitialAccount(AccountRequest accountRequest) {
        accountDomainService.create(
                new CustomerId(accountRequest.customerId()),
                AccountType.valueOf(accountRequest.type().name()),
                accountRequest.initialBalance());
    }
}
