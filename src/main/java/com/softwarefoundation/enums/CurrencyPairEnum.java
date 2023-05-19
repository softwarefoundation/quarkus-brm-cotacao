package com.softwarefoundation.enums;

import lombok.Getter;

@Getter
public enum CurrencyPairEnum {

    USD_BRL("USD-BRL");

    private String pair;

    CurrencyPairEnum(String pair) {
        this.pair = pair;
    }
}
