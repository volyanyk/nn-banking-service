package com.example.account.domain.exception;

import com.example.common.domain.exception.DomainException;

public class IncorrectAccountCurrencyException extends DomainException {
    public IncorrectAccountCurrencyException() {
        super("Both accounts must have different type");
    }
}
