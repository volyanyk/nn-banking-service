package com.example.account.domain;

import com.example.account.domain.exception.IncorrectBalanceException;
import com.example.account.domain.exception.NegativeDebitException;
import com.example.account.domain.exception.NullInputException;
import com.example.common.domain.entity.BaseEntity;
import com.example.common.domain.valueobject.AccountId;
import com.example.common.domain.valueobject.CustomerId;
import com.example.mq.client.account.AccountType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
public class AccountDomain extends BaseEntity<AccountId> {
    @NotNull
    private final CustomerId customerId;
    @NotNull
    private final AccountType type;
    @NotNull
    private BigDecimal balance;

    public AccountDomain(AccountId id,
                         CustomerId customerId,
                         AccountType type,
                         BigDecimal balance) {
        super.setId(id);
        this.validateBalance(balance);
        this.customerId = customerId;
        this.type = type;
    }


    private void validateBalance(BigDecimal balance) {
        if(balance == null) {
            this.balance = BigDecimal.ZERO;
            return;
        } else if(balance.compareTo(BigDecimal.ZERO) < 0){
            throw new IncorrectBalanceException(balance);
        }
        this.balance = balance;
    }


    public void addBalance(BigDecimal value) {
        this.balance = this.balance.add(value);
    }

    public void subBalance(BigDecimal value) {

        BigDecimal subtract = this.balance.subtract(value);
        if(subtract.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeDebitException(value, this.balance);
        }
        this.balance = this.balance.subtract(value);

    }
}
