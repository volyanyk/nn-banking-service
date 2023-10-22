package com.example.customer.exception;

import com.example.common.domain.exception.DomainException;

public class DomainValidationException extends DomainException{
    public DomainValidationException(String message) {
        super(message);
    }
}
