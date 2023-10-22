package com.example.mq.client.customer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "customer",
        url = "${clients.customer.url}"
)
public interface CustomerClient {

    @GetMapping("api/v1/customers/{id}")
    CustomerDataResponse getCustomerById(@PathVariable Integer id);
}
