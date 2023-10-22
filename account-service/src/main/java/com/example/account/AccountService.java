package com.example.customer;


import com.example.api.exception.ResourceNotFoundException;
import com.example.customer.exception.IncorrectCustomerInputDataException;
import com.example.customer.model.CustomerDataResponse;
import com.example.customer.model.CustomerRegistrationRequest;
import com.example.mq.client.account.AccountType;
import com.example.mq.client.account.NewAccountRequest;
import com.example.mq.client.notification.NewNotificationRequest;
import com.example.mq.producer.RabbitMQMessageProducer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;

    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();
        customerRepository.saveAndFlush(customer);
        NewAccountRequest accountRequest = new NewAccountRequest(
                customer.getId(),
                request.initialBalance(),
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

    }

    public List<Integer> getCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(Customer::getId)
        .toList();
    }
    public CustomerDataResponse getCustomerById(final Integer id) {
        if(Objects.isNull(id)){
            throw new IncorrectCustomerInputDataException();
        }
        return customerRepository.findById(id)
                .map(customer ->
                        new CustomerDataResponse(
                                id,
                                customer.getFirstName(),
                                customer.getLastName(),
                                customer.getEmail()))
                .orElseThrow(() ->
                        new ResourceNotFoundException("The customer with the id " + id + " was not found."));
    }
}
