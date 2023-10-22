package com.example.account.domain.exception;

import com.example.common.domain.exception.DomainException;

public class IncorrectTransferAccountSizeException extends DomainException {

    public IncorrectTransferAccountSizeException(int size) {
        super("Expected accounts: 2; Given: " + size);
    }
}
