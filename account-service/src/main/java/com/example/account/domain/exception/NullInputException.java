package com.example.account.domain.exception;

import com.example.common.domain.exception.DomainException;

public class NullInputException extends DomainException {
    public NullInputException(Object o) {
        super("Input element: " + o.getClass().getName() + " is null");
    }
}
