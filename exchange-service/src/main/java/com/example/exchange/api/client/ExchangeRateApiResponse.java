package com.example.exchange.api.client;

import java.util.ArrayList;

public record ExchangeRateApiResponse(String table, String currency, String code, ArrayList<Rate> rates) {
    public record Rate(String no,
            String effectiveDate,
            double mid) {
    }
}
