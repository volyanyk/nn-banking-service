package com.example.account.domain;

import com.example.account.domain.exception.IncorrectAccountCurrencyException;
import com.example.account.domain.exception.IncorrectTransferAccountSizeException;
import com.example.common.domain.entity.AggregateRoot;
import com.example.common.domain.valueobject.AccountId;
import com.example.common.domain.valueobject.TransferId;
import com.example.mq.client.account.AccountType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class AccountTransferDomain extends AggregateRoot<TransferId> {
    final List<AccountDomain> accountDomains;

    public AccountTransferDomain(List<AccountDomain> accountDomains) {
        if(accountDomains.size() != 2) {
            throw new IncorrectTransferAccountSizeException(accountDomains.size());
        }
        this.accountDomains = accountDomains;
    }

    public void buy(AccountId fromId,AccountId toId, BigDecimal volume, BigDecimal rate) {
        var typedValue = volume.multiply(rate);
        process(fromId, toId, typedValue, volume);
    }

    public List<AccountDomain> getAccounts() {
        return this.accountDomains;
    }

    public void sell(AccountId fromId, AccountId toId, BigDecimal volume, BigDecimal rate) {
        var typedValue = volume.multiply(rate);
        process(fromId, toId, volume, typedValue);

    }

    private void process(AccountId fromId, AccountId toId, BigDecimal volume1, BigDecimal volume2) {
        AccountDomain sellAccount = this.accountDomains.stream().filter(accountDomain -> accountDomain.getId().equals(fromId)).findAny().get();
        sellAccount.subBalance(volume1);
        AccountDomain buyAccount = this.accountDomains.stream().filter(accountDomain -> accountDomain.getId().equals(toId)).findAny().get();
        buyAccount.addBalance(volume2);
    }
}
