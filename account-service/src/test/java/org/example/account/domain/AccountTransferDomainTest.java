package org.example.account.domain;

import com.example.account.domain.AccountDomain;
import com.example.account.domain.AccountTransferDomain;
import com.example.account.domain.exception.IncorrectTransferAccountSizeException;
import com.example.common.domain.valueobject.AccountId;
import com.example.common.domain.valueobject.CustomerId;
import com.example.mq.client.account.AccountType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountTransferDomainTest {

    @Test
    void constructor_shouldThrowExceptionForIncorrectAccountSize() {
        assertThrows(IncorrectTransferAccountSizeException.class, () -> {
            new AccountTransferDomain(Arrays.asList(
                    new AccountDomain(
                    new AccountId(new Random().nextInt()),
                    new CustomerId(new Random().nextInt()),
                    AccountType.PLN,
                    BigDecimal.ONE
            ), new AccountDomain(
                    new AccountId(new Random().nextInt()),
                    new CustomerId(new Random().nextInt()),
                    AccountType.USD,
                    BigDecimal.ZERO
            ),new AccountDomain(
                    new AccountId(new Random().nextInt()),
                    new CustomerId(new Random().nextInt()),
                    AccountType.PLN,
                    BigDecimal.TEN
            )));
        });
    }

    @Test
    void buy_shouldTransferFundsFromOneAccountToAnother() {
        AccountDomain fromAccount = new AccountDomain(
                new AccountId(new Random().nextInt()),
                new CustomerId(new Random().nextInt()),
                AccountType.PLN,
                BigDecimal.TEN
        );
        AccountDomain toAccount = new AccountDomain(
                new AccountId(new Random().nextInt()),
                new CustomerId(new Random().nextInt()),
                AccountType.USD,
                BigDecimal.TEN
        );

        AccountTransferDomain transferDomain = new AccountTransferDomain(Arrays.asList(fromAccount, toAccount));
        transferDomain.buy(fromAccount.getId(), toAccount.getId(), new BigDecimal(1L), new BigDecimal(10L));

        assertEquals(new BigDecimal(0L), fromAccount.getBalance());
        assertEquals(new BigDecimal(11L), toAccount.getBalance());
    }
    @Test
    void buy_shouldTransferFundsFromOneAccountToAnother2() {
        AccountDomain fromAccount = new AccountDomain(
                new AccountId(new Random().nextInt()),
                new CustomerId(new Random().nextInt()),
                AccountType.PLN,
                new BigDecimal(100L)
        );
        AccountDomain toAccount = new AccountDomain(
                new AccountId(new Random().nextInt()),
                new CustomerId(new Random().nextInt()),
                AccountType.USD,
                BigDecimal.ZERO
        );

        AccountTransferDomain transferDomain = new AccountTransferDomain(Arrays.asList(fromAccount, toAccount));
        transferDomain.buy(fromAccount.getId(), toAccount.getId(), new BigDecimal(5L), new BigDecimal(10L));

        assertEquals(new BigDecimal(50L), fromAccount.getBalance());
        assertEquals(new BigDecimal(5L), toAccount.getBalance());
    }

    @Test
    void sell_shouldTransferFundsFromOneAccountToAnother() {
        AccountDomain fromAccount = new AccountDomain(
                new AccountId(new Random().nextInt()),
                new CustomerId(new Random().nextInt()),
                AccountType.PLN,
                new BigDecimal(100L)
        );
        AccountDomain toAccount = new AccountDomain(
                new AccountId(new Random().nextInt()),
                new CustomerId(new Random().nextInt()),
                AccountType.USD,
                new BigDecimal(0L)
        );

        AccountTransferDomain transferDomain = new AccountTransferDomain(Arrays.asList(fromAccount, toAccount));
        transferDomain.sell(fromAccount.getId(), toAccount.getId(), new BigDecimal(5L), new BigDecimal(10L));

        assertEquals(new BigDecimal(95L), fromAccount.getBalance());
        assertEquals(new BigDecimal(50L), toAccount.getBalance());
    }

    @Test
    void getAccounts_shouldReturnListOfAccounts() {
        AccountDomain account1 = new AccountDomain(
                new AccountId(new Random().nextInt()),
                new CustomerId(new Random().nextInt()),
                AccountType.USD,
                BigDecimal.ZERO
        );
        AccountDomain account2 = new AccountDomain(
                new AccountId(new Random().nextInt()),
                new CustomerId(new Random().nextInt()),
                AccountType.USD,
                BigDecimal.ZERO
        );

        AccountTransferDomain transferDomain = new AccountTransferDomain(Arrays.asList(account1, account2));

        assertEquals(Arrays.asList(account1, account2), transferDomain.getAccounts());
    }
}