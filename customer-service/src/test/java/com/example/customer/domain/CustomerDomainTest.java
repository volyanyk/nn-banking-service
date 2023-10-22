package com.example.customer.domain;

import com.example.common.domain.exception.DomainException;
import com.example.common.domain.valueobject.CustomerId;
import com.example.customer.exception.DomainValidationException;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomerDomainTest {

    @Test
    void constructorShouldSetFieldsCorrectly() {
        CustomerId customerId = new CustomerId(new Random().nextInt());
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";

        CustomerDomain customer = new CustomerDomain(customerId, firstName, lastName, email);

        assertEquals(customerId, customer.getId());
        assertEquals(firstName, customer.getFirstName());
        assertEquals(lastName, customer.getLastName());
        assertEquals(email, customer.getEmail());
    }


    @Test
    void constructorShouldThrowExceptionForNullFirstName() {
        CustomerId customerId = new CustomerId(new Random().nextInt());
        String lastName = "Doe";
        String email = "john.doe@example.com";

        assertThrows(DomainValidationException.class, () -> new CustomerDomain(customerId, null, lastName, email));
    }

    @Test
    void constructorShouldThrowExceptionForEmptyFirstName() {
        CustomerId customerId = new CustomerId(new Random().nextInt());
        String lastName = "Doe";
        String email = "john.doe@example.com";

        assertThrows(DomainValidationException.class, () -> new CustomerDomain(customerId, "", lastName, email));
    }

    @Test
    void constructorShouldThrowExceptionForNullLastName() {
        CustomerId customerId = new CustomerId(new Random().nextInt());
        String firstName = "John";
        String email = "john.doe@example.com";

        assertThrows(DomainValidationException.class, () -> new CustomerDomain(customerId, firstName, null, email));
    }

    @Test
    void constructorShouldThrowExceptionForEmptyLastName() {
        CustomerId customerId = new CustomerId(new Random().nextInt());
        String firstName = "John";
        String email = "john.doe@example.com";

        assertThrows(DomainValidationException.class, () -> new CustomerDomain(customerId, firstName, "", email));
    }


}
