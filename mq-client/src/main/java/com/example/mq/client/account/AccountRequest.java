package com.example.mq.client.account;

import java.math.BigDecimal;

public record NewAccountRequest(
        Integer customerId,
        BigDecimal initialBalance,
        AccountType type
) {
}
