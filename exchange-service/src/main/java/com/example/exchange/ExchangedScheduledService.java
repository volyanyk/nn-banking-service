package com.example.exchange;

import com.example.exchange.api.client.ExchangeRateApiResponse;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@Service
public class ExchangedScheduledService {
    @Value("${api.nbp-url}")
    private String apiUrl;

    private final ExchangeRepository exchangeRepository;
    private final RestTemplate restTemplate;

    public ExchangedScheduledService(ExchangeRepository exchangeRepository, RestTemplate restTemplate) {
        this.exchangeRepository = exchangeRepository;
        this.restTemplate = restTemplate;
    }

    @Scheduled(cron = "0 0 0 * * *") // Midnight cron job
    public void cronFetchAndStoreExchangeRate() {
        log.info("The cron job for storing the exchange rate is started");
        this.fetchAndStoreExchangeRate();
    }
    @PostConstruct
    public void postConstructFetchExchangeRate(){
        log.info("The post construct for storing the exchange rate is started");
        this.fetchAndStoreExchangeRate();
    }

    private void fetchAndStoreExchangeRate() {
        String apiEndpoint = apiUrl + "/api/exchangerates/rates/a/usd";
        ExchangeRateApiResponse response = restTemplate.getForObject(apiEndpoint, ExchangeRateApiResponse.class);

        if (response != null && response.rates() != null && !response.rates().isEmpty()) {
            ExchangeRateApiResponse.Rate rate = response.rates().get(0);
            String no = rate.no();
            LocalDate effectiveDate = LocalDate.parse(rate.effectiveDate());

            if (exchangeRepository.findByNoAndEffectiveDate(no, effectiveDate).isEmpty()) {
                Exchange exchangeRate = new Exchange();
                exchangeRate.setNo(no);
                exchangeRate.setEffectiveDate(effectiveDate);
                exchangeRate.setCurrency(response.currency());
                exchangeRate.setCode(response.code());
                exchangeRate.setMid(BigDecimal.valueOf(rate.mid()));
                exchangeRepository.save(exchangeRate);
                log.info("The exchange rate was stored.");
            }
        }
    }


}
