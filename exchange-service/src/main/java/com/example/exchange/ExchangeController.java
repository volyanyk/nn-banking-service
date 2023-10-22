package com.example.customer;

import com.example.customer.model.CustomerDataResponse;
import com.example.customer.model.CustomerRegistrationRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/customers")
@AllArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public void registerCustomer(@RequestBody CustomerRegistrationRequest customerRegistrationRequest) {
        log.info("new customer registration {}", customerRegistrationRequest);
        customerService.registerCustomer(customerRegistrationRequest);
    }
    @GetMapping
    public ResponseEntity<List<Integer>> getCustomers() {
        log.info("get customer ids");
        List<Integer> customerIds = customerService.getCustomers();
        return ResponseEntity.ok(customerIds);
    }
    @GetMapping(path = "{customerId}")
    public ResponseEntity<CustomerDataResponse> getCustomer(@PathVariable("customerId") Integer customerId) {
        log.info("get customer id by id {}", customerId);
        return ResponseEntity.ok(customerService.getCustomerById(customerId));
    }
}
