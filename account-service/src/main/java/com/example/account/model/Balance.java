package com.example.account.model;

import java.math.BigDecimal;

public record Balance(Integer id, BigDecimal value, String currency) {
}
