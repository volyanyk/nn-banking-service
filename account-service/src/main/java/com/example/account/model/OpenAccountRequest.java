package com.example.account.model;

import com.example.mq.client.account.AccountType;

import java.math.BigDecimal;

public record OpenAccountRequest(BigDecimal initialBalance,
                                 String currency) {
}
