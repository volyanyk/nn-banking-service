package com.example.customer.domain;

import com.example.common.domain.valueobject.CustomerId;
import com.example.customer.Customer;
import com.example.customer.CustomerRepository;
import com.example.mq.client.account.AccountRequest;
import com.example.mq.client.account.AccountType;
import com.example.mq.client.notification.NewNotificationRequest;
import com.example.mq.producer.RabbitMQMessageProducer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@AllArgsConstructor
public class CustomerDomainService {
    private final CustomerAclConverter customerAclConverter;
    private final CustomerRepository customerRepository;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;
    public CustomerDomain registerCustomer(String firstName, String lastName, String email, BigDecimal initialBalance) {
        CustomerDomain customerDomain = customerAclConverter.create(firstName, lastName, email);
        Customer customer = Customer.builder()
                .firstName(customerDomain.getFirstName())
                .lastName(customerDomain.getLastName())
                .email(customerDomain.getEmail())
                .build();
        customerRepository.saveAndFlush(customer);
        customerDomain.setId(new CustomerId(customer.getId()));
        AccountRequest accountRequest = new AccountRequest(
                customer.getId(),
                initialBalance,
                AccountType.PLN

        );
        rabbitMQMessageProducer.publish(accountRequest,
                "internal.exchange",
                "internal.account.routing-key");


        NewNotificationRequest notificationRequest = new NewNotificationRequest(
                customer.getId(),
                customer.getEmail(),
                String.format("Hi Mr/Mrs %s, welcome to our bank!",
                        customer.getFirstName())
        );
        rabbitMQMessageProducer.publish(
                notificationRequest,
                "internal.exchange",
                "internal.notification.routing-key"
        );
        return customerDomain;
    }
}
