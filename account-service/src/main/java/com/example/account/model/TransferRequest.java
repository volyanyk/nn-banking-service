package com.example.account.model;

import java.math.BigDecimal;

public record TransferRequest(Integer fromId, Integer toId, BigDecimal volume) {
}
