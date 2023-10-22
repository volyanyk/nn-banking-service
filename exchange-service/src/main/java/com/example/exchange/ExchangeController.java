package com.example.exchange;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Slf4j
@RestController
@RequestMapping("api/v1/exchange")
@AllArgsConstructor
public class ExchangeController {

    private final ExchangeService exchangeService;


    @GetMapping("/{currency}")
    public ResponseEntity<BigDecimal> getByCurrency(@PathVariable String currency) {
        log.info("get customer ids");
        BigDecimal val = exchangeService.getByCurrency(currency);
        return ResponseEntity.ok(val);
    }
}
