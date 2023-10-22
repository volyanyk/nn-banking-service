package com.example.mq.client.exchange;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

@FeignClient(
        name = "exchange",
        url = "${clients.exchange.url}"
)
public interface ExchangeClient {

    @GetMapping("api/v1/exchange/{currency}")
    BigDecimal getByCurrency(@PathVariable String currency);
}
