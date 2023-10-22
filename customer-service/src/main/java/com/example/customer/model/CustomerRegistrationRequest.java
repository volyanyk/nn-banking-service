package com.example.customer.model;

import java.math.BigDecimal;

public record CustomerRegistrationRequest(
        String firstName,
        String lastName,
        String email,
        BigDecimal initialBalance) {
}
