package com.silverBarsMarketplace;

import java.util.Currency;

/**
 * The Price class represents price with Currency and Value attributes
 */

public class Price implements Comparable<Price> {
    private Currency currency;
    private Integer value;

    public Price (Currency currency, Integer value) {
        this.currency = currency;
        this.value = value;
    }

    public String toString() {
        return String.format("%s%s", currency.getSymbol(), value.toString());
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public Integer getValue() {
        return this.value;
    }

    @Override
    public int compareTo(Price price) {
        return currency.getCurrencyCode().compareTo(price.currency.getCurrencyCode()) + value.compareTo(price.value);
    }
}
