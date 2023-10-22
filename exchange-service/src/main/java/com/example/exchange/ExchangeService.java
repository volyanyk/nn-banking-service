package com.example.exchange;

import com.example.api.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ExchangeService {
    private final ExchangeRepository exchangeRepository;

    public BigDecimal getByCurrency(String currency) {
        Optional<Exchange> byCurrencyOrderByEffectiveDateDesc = exchangeRepository.findByCodeOrderByEffectiveDateDesc(currency);
        if(byCurrencyOrderByEffectiveDateDesc.isEmpty()) {
            throw new ResourceNotFoundException("No values for this criteria found");
        }
        return byCurrencyOrderByEffectiveDateDesc.get().getMid();
    }
}
