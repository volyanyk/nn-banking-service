package org.example.account.domain;

import com.example.account.Account;
import com.example.account.AccountRepository;
import com.example.account.domain.AccountAclConverter;
import com.example.account.domain.AccountDomain;
import com.example.account.domain.AccountDomainService;
import com.example.account.domain.exception.IncorrectTransferAccountSizeException;
import com.example.api.exception.ResourceNotFoundException;
import com.example.common.domain.valueobject.AccountId;
import com.example.common.domain.valueobject.CustomerId;
import com.example.mq.client.account.AccountType;
import com.example.mq.client.customer.CustomerClient;
import com.example.mq.client.customer.CustomerDataResponse;
import com.example.mq.client.exchange.ExchangeClient;
import com.example.mq.producer.RabbitMQMessageProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountDomainServiceTest {

    @Mock
    private CustomerClient customerClient;

    @Mock
    private ExchangeClient exchangeClient;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private RabbitMQMessageProducer rabbitMQMessageProducer;

    @Mock
    private AccountAclConverter accountAclConverter;

    @InjectMocks
    private AccountDomainService accountDomainService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAccount_Success() {
        Integer customerId = 123;
        AccountType type = AccountType.PLN;
        BigDecimal initialBalance = BigDecimal.valueOf(100.0);

        CustomerDataResponse customerDataResponse = new CustomerDataResponse("John", "Doe", "john@example.com");

        when(customerClient.getCustomerById(customerId)).thenReturn(customerDataResponse);
        when(accountAclConverter.create(any(), any(), any())).thenReturn(createMockAccountDomain());

        AccountDomain createdAccount = accountDomainService.create(new CustomerId(customerId), type, initialBalance);

        assertNotNull(createdAccount);
        assertEquals(new CustomerId(customerId), createdAccount.getCustomerId());
        assertEquals(type, createdAccount.getType());
        assertEquals(initialBalance, createdAccount.getBalance());

        verify(accountRepository, times(1)).saveAndFlush(any());
        verify(rabbitMQMessageProducer, times(1)).publish(any(), any(), any());
    }

    @Test
    void testCreateAccount_CustomerNotFound() {
        Integer customerId = 123;
        AccountType type = AccountType.PLN;
        BigDecimal initialBalance = BigDecimal.valueOf(100.0);

        when(customerClient.getCustomerById(customerId)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () ->
                accountDomainService.create(new CustomerId(customerId), type, initialBalance));

        verify(accountRepository, never()).saveAndFlush(any());
        verify(rabbitMQMessageProducer, never()).publish(any(), any(), any());
    }

    @Test
    void testTransfer_CustomerNotFound() {
        Integer customerId = 123;
        Integer fromId = 1;
        Integer toId = 2;
        BigDecimal volume = BigDecimal.valueOf(50.0);

        when(customerClient.getCustomerById(customerId)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () ->
                accountDomainService.transfer(new CustomerId(customerId), fromId, toId, volume));

        verify(accountRepository, never()).saveAllAndFlush(anyList());
        verify(exchangeClient, never()).getByCurrency(anyString());
    }

    @Test
    void testTransfer_IncorrectSourceAccount() {
        Integer customerId = 123;
        Integer fromId = 10;
        Integer toId = 2;
        BigDecimal volume = BigDecimal.valueOf(50.0);

        when(customerClient.getCustomerById(customerId)).thenReturn(new CustomerDataResponse("John", "Doe", "john@example.com"));
        when(accountRepository.findByCustomerId(any())).thenReturn(createMockAccounts());
        when(exchangeClient.getByCurrency(anyString())).thenReturn(BigDecimal.ONE);

        assertThrows(IncorrectTransferAccountSizeException.class, () ->
                accountDomainService.transfer(new CustomerId(customerId), fromId, toId, volume));

        verify(accountRepository, never()).saveAllAndFlush(anyList());
        verify(exchangeClient, never()).getByCurrency(anyString());
    }

    private AccountDomain createMockAccountDomain() {
        return new AccountDomain(
                new AccountId(1),
                new CustomerId(123),
                AccountType.PLN,
                BigDecimal.valueOf(100.0)
        );
    }

    private List<Account> createMockAccounts() {
        Account account1 = new Account(1,  123,AccountType.PLN, BigDecimal.valueOf(100.0));
        Account account2 = new Account(2, 123, AccountType.USD, BigDecimal.valueOf(200.0));

        return Arrays.asList(account1, account2);
    }
}
