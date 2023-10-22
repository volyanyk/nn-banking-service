package com.example.account.domain.exception;

import com.example.common.domain.exception.DomainException;

import java.math.BigDecimal;

public class NegativeDebitException extends DomainException {
    public NegativeDebitException(BigDecimal value, BigDecimal balance) {
        super("Customer's account does not have enough money. Balance: " + balance + ", requested value: " + value);
    }
}
