package com.example.customer.exception;

public class IncorrectCustomerInputData extends RuntimeException{

    public IncorrectCustomerInputData() {
        super("Incorrect input data");
    }
}
