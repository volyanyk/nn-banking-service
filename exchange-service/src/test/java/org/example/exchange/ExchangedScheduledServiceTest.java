package org.example.exchange;

import com.example.exchange.Exchange;
import com.example.exchange.ExchangeRepository;
import com.example.exchange.ExchangedScheduledService;
import com.example.exchange.api.client.ExchangeRateApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class ExchangedScheduledServiceTest {
    @Mock
    private ExchangeRepository exchangeRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ExchangedScheduledService exchangedScheduledService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void fetchAndStoreExchangeRate_shouldFetchAndStoreData() {
        ExchangeRateApiResponse.Rate rate = new ExchangeRateApiResponse.Rate(
                "204/A/NBP/2023", "2023-10-20",4.2194);

        ExchangeRateApiResponse response = new ExchangeRateApiResponse(
                "A",
                "dolar amerykański",
                "USD",
                new ArrayList(List.of(rate)));

        when(exchangeRepository.findByNoAndEffectiveDate(anyString(), any(LocalDate.class)))
                .thenReturn(Optional.empty());

        when(restTemplate.getForObject(anyString(), eq(ExchangeRateApiResponse.class)))
                .thenReturn(response);

        exchangedScheduledService.cronFetchAndStoreExchangeRate();

        verify(exchangeRepository, times(1)).save(any(Exchange.class));
    }
}
