package com.example.exchange;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Exchange {


    @Id
    @SequenceGenerator(
            name = "exchange_id_sequence",
            sequenceName = "exchange_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "exchange_id_sequence"
    )
    private Integer id;
    private String no;
    private LocalDate effectiveDate;
    private String currency;
    private String code;
    private BigDecimal mid;
}
