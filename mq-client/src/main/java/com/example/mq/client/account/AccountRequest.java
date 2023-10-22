package com.example.mq.client.account;

import java.math.BigDecimal;

public record AccountRequest(
        Integer customerId,
        BigDecimal initialBalance,
        AccountType type
) {
}
