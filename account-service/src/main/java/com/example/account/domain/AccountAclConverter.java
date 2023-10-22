package com.example.account.domain;

import com.example.common.domain.valueobject.AccountId;
import com.example.common.domain.valueobject.CustomerId;
import com.example.mq.client.account.AccountType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountAclConverter {

    public AccountDomain create(CustomerId customerId, AccountType type, BigDecimal initialBalance) {
        return new AccountDomain(null, customerId, type, initialBalance);
    }
    public AccountDomain build(AccountId accountId, CustomerId customerId, AccountType type, BigDecimal initialBalance) {
        return new AccountDomain(accountId, customerId, type, initialBalance);
    }
}
