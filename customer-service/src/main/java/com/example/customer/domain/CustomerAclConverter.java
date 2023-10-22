package com.example.customer.domain;

import com.example.common.domain.valueobject.AccountId;
import com.example.common.domain.valueobject.CustomerId;
import com.example.mq.client.account.AccountType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CustomerAclConverter {

    public CustomerDomain create(String firstName, String lastName, String email) {
        return new CustomerDomain(null, firstName, lastName, email);
    }
}
