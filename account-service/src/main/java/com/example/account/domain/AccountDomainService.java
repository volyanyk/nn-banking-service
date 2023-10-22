package com.example.account.domain;

import com.example.account.Account;
import com.example.account.AccountRepository;
import com.example.api.exception.ResourceNotFoundException;
import com.example.common.domain.valueobject.AccountId;
import com.example.common.domain.valueobject.CustomerId;
import com.example.mq.client.account.AccountType;
import com.example.mq.client.customer.CustomerClient;
import com.example.mq.client.customer.CustomerDataResponse;
import com.example.mq.client.exchange.ExchangeClient;
import com.example.mq.client.notification.NewNotificationRequest;
import com.example.mq.producer.RabbitMQMessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AccountDomainService {
    private final CustomerClient customerClient;
    private final ExchangeClient exchangeClient;
    private final AccountRepository accountRepository;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;
    private final AccountAclConverter accountAclConverter;

    public AccountDomainService(CustomerClient customerClient,
                                ExchangeClient exchangeClient,
                                AccountRepository accountRepository,
                                RabbitMQMessageProducer rabbitMQMessageProducer,
                                AccountAclConverter accountAclConverter) {
        this.customerClient = customerClient;
        this.exchangeClient = exchangeClient;
        this.accountRepository = accountRepository;
        this.rabbitMQMessageProducer = rabbitMQMessageProducer;
        this.accountAclConverter = accountAclConverter;
    }

    public AccountDomain create(CustomerId customerId, AccountType type, BigDecimal initialBalance) {
    return Optional.ofNullable(customerClient.getCustomerById(customerId.getValue())).map(customerDataResponse -> {
        AccountDomain accountDomain = accountAclConverter.create(customerId, type, initialBalance);
        Account account = Account.builder()
                .type(accountDomain.getType())
                .customerId(customerId.getValue())
                .balance(accountDomain.getBalance())
                .build();
        accountRepository.saveAndFlush(account);
        accountDomain.setId(new AccountId(account.getId()));
        NewNotificationRequest notificationRequest = new NewNotificationRequest(
                customerId.getValue(),
                customerDataResponse.email(),
                String.format("Hi Mr/Mrs %s, the account in currency %s was opened!",
                        customerDataResponse.firstName(), type)
        );
        rabbitMQMessageProducer.publish(
                notificationRequest,
                "internal.exchange",
                "internal.notification.routing-key"
        );
        return accountDomain;
    }).orElseThrow(() -> new ResourceNotFoundException("Customer with id: " + customerId + " was not found"));
    }

    public List<AccountDomain> transfer(CustomerId customerId, Integer fromId, Integer toId, BigDecimal volume) {
        Optional<CustomerDataResponse> customerById = Optional.ofNullable(customerClient.getCustomerById(customerId.getValue()));
        if(customerById.isEmpty()) {
            throw new ResourceNotFoundException("Customer with id: " + customerId + " was not found");
        }

        AccountTransferDomain transferDomain =
                new AccountTransferDomain(
                        accountRepository.findByCustomerId(
                                customerId.getValue())
                                .stream()
                                .filter(account -> Arrays.asList(fromId, toId).contains(account.getId()))
                                .map(account ->
                                        accountAclConverter.build(
                                                new AccountId(account.getId()),
                                                new CustomerId(account.getCustomerId()),
                                                account.getType(), account.getBalance())).toList());


        String destinationCurrency = getCurrency(transferDomain, toId, "Incorrect destination account");
        String sourceCurrency = getCurrency(transferDomain, fromId, "Incorrect source account");
        if(!destinationCurrency.equals(AccountType.PLN.name())){
            transferDomain.buy(
                    new AccountId(fromId),
                    new AccountId(toId),
                    volume,
                    exchangeClient.getByCurrency(destinationCurrency));
        } else {
            transferDomain.sell(
                    new AccountId(fromId),
                    new AccountId(toId),
                    volume,
                    exchangeClient.getByCurrency(sourceCurrency));
        }

        List<AccountDomain> transferredAccounts = transferDomain.getAccounts();
        accountRepository.saveAllAndFlush(transferredAccounts.
                stream()
                .map(accountDomain -> Account.builder()
                    .id(accountDomain.getId().getValue())
                    .type(accountDomain.getType())
                    .customerId(customerId.getValue())
                    .balance(accountDomain.getBalance())
                        .build())
                .toList());
        return transferDomain.getAccounts();
    }

    private static String getCurrency(AccountTransferDomain transferDomain, Integer fromId, String Incorrect_source_account) {
        return transferDomain.getAccounts()
                .stream()
                .filter(accountDomain -> accountDomain != null &&
                        accountDomain.getId().getValue().equals(fromId))
                .map(accountDomain -> accountDomain.getType().name()).findAny().orElseThrow(() -> new ResourceNotFoundException(Incorrect_source_account));
    }
}
