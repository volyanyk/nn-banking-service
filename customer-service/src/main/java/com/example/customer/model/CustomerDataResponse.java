package com.example.customer.model;

public record CustomerDataResponse(
        Integer id,
        String firstName,
        String lastName,
        String email) {
}
