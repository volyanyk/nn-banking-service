package com.example.account.domain.exception;

import com.example.common.domain.exception.DomainException;

import java.math.BigDecimal;

public class IncorrectBalanceException extends DomainException {

    public IncorrectBalanceException(BigDecimal balance) {
        super("The account balance is incorrect. Value:" + balance);
    }
}
