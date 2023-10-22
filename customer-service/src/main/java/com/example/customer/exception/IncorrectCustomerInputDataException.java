package com.example.customer.exception;

public class IncorrectCustomerInputDataException extends RuntimeException{

    public IncorrectCustomerInputDataException() {
        super("Incorrect input data");
    }
}
