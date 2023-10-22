package org.example.account.domain;

import com.example.account.domain.AccountDomain;
import com.example.account.domain.exception.IncorrectBalanceException;
import com.example.account.domain.exception.NegativeDebitException;
import com.example.common.domain.valueobject.AccountId;
import com.example.common.domain.valueobject.CustomerId;
import com.example.mq.client.account.AccountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountDomainTest {

    private AccountDomain account;

    final int accId = new Random().nextInt();
    final int custId = new Random().nextInt();
    final AccountType accountType = AccountType.PLN;
    @BeforeEach
    void setUp() {
        AccountId accountId = new AccountId(accId);
        CustomerId customerId = new CustomerId(custId);
        AccountType accountType = AccountType.PLN;
        BigDecimal initialBalance = BigDecimal.valueOf(1000);

        account = new AccountDomain(accountId, customerId, accountType, initialBalance);
    }

    @Test
    void constructorShouldSetValuesCorrectly() {
        assertEquals(accId, account.getId().getValue());
        assertEquals(custId, account.getCustomerId().getValue());
        assertEquals(AccountType.PLN, accountType);
        assertEquals(BigDecimal.valueOf(1000), account.getBalance());
    }

    @Test
    void constructorShouldHandleNullBalance() {
        AccountDomain accountWithNullBalance = new AccountDomain(
                new AccountId(accId),
                new CustomerId(custId),
                accountType,
                null
        );

        assertEquals(BigDecimal.ZERO, accountWithNullBalance.getBalance());
    }

    @Test
    void addBalanceShouldIncreaseBalance() {
        account.addBalance(BigDecimal.valueOf(500));

        assertEquals(BigDecimal.valueOf(1500), account.getBalance());
    }

    @Test
    void subBalanceShouldDecreaseBalance() {
        account.subBalance(BigDecimal.valueOf(500));

        assertEquals(BigDecimal.valueOf(500), account.getBalance());
    }

    @Test
    void subBalanceShouldThrowExceptionForNegativeDebit() {
        assertThrows(NegativeDebitException.class, () -> account.subBalance(BigDecimal.valueOf(1500)));
    }

    @Test
    void validateBalanceShouldThrowExceptionForNegativeBalance() {
        assertThrows(IncorrectBalanceException.class, () -> new AccountDomain(
                new AccountId(accId),
                new CustomerId(custId),
                AccountType.PLN,
                BigDecimal.valueOf(-500)
        ));
    }
}