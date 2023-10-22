package com.example.customer.domain;

import com.example.customer.Customer;
import com.example.customer.CustomerRepository;
import com.example.mq.producer.RabbitMQMessageProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerDomainServiceTest {

    @Mock
    private CustomerAclConverter customerAclConverter;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private RabbitMQMessageProducer rabbitMQMessageProducer;

    @InjectMocks
    private CustomerDomainService customerDomainService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerCustomer_shouldSaveCustomerAndPublishMessages() {
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        BigDecimal initialBalance = BigDecimal.valueOf(1000);

        CustomerDomain customerDomain = new CustomerDomain(null, firstName,lastName,email);
        when(customerAclConverter.create(firstName, lastName, email)).thenReturn(customerDomain);

        Customer customer = Customer.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .id(1)
                .build();
        when(customerRepository.saveAndFlush(any())).thenReturn(customer);

        CustomerDomain result = customerDomainService.registerCustomer(firstName, lastName, email, initialBalance);

        verify(customerAclConverter).create(firstName, lastName, email);

        verify(rabbitMQMessageProducer, times(2)).publish(any(), any(), any());

        assertNotNull(result);
        assertEquals(customerDomain, result);
    }
}
