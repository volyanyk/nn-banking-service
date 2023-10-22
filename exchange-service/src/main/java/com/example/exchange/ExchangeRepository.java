package com.example.exchange;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface ExchangeRepository extends JpaRepository<Exchange, Integer> {
    Optional<Exchange> findByNoAndEffectiveDate(String no, LocalDate effectiveDate);

    Optional<Exchange> findByCodeOrderByEffectiveDateDesc(String currency);
}
