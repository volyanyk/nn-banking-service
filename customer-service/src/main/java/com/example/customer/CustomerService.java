package com.example.customer;


import com.example.api.exception.ResourceNotFoundException;
import com.example.common.domain.valueobject.CustomerId;
import com.example.customer.domain.CustomerDomain;
import com.example.customer.domain.CustomerDomainService;
import com.example.customer.exception.IncorrectCustomerInputDataException;
import com.example.customer.model.CustomerDataResponse;
import com.example.customer.model.CustomerRegistrationRequest;
import com.example.mq.producer.RabbitMQMessageProducer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerDomainService customerDomainService;

    public CustomerDataResponse registerCustomer(CustomerRegistrationRequest request) {
        CustomerDomain customerDomain = customerDomainService.registerCustomer(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.initialBalance());
        return new CustomerDataResponse(
                customerDomain.getId().getValue(),
                customerDomain.getFirstName(),
                customerDomain.getLastName(),
                customerDomain.getEmail());
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
