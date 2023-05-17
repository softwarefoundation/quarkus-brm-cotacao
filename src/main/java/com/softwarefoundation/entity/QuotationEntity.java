package com.softwarefoundation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@Data
@Entity
@Table(name = "TB01_QUOTATION")
public class QuotationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "DATE")
    private Date date;

    @Column(name = "CURRENCY_PRICE")
    private BigDecimal currencyPrice;

    @Column(name = "PCT_CHANGE")
    private String pctChange;

    @Column(name = "PAIR")
    private String pair;
}
