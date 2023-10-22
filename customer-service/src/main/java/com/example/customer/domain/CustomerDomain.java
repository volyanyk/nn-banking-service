package com.example.customer.domain;

import com.example.common.domain.entity.BaseEntity;
import com.example.common.domain.valueobject.CustomerId;
import com.example.customer.exception.DomainValidationException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;


@Getter
public class CustomerDomain extends BaseEntity<CustomerId> {
    private final String firstName;
    private final String lastName;
    private final String email;

    public CustomerDomain(CustomerId id, String firstName, String lastName, String email) {
        super.setId(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.validateInput(firstName, lastName, email);
    }

    private void validateInput(String firstName, String lastName, String email) {
        if(StringUtils.isEmpty(firstName)) throw new DomainValidationException("First name is empty");
        if(StringUtils.isEmpty(lastName)) throw new DomainValidationException("Last name is empty");
        if(StringUtils.isEmpty(email)) throw new DomainValidationException("Email name is empty");
    }
}
