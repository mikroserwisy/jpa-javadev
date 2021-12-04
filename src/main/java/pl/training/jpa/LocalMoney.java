package pl.training.jpa;

import org.javamoney.moneta.FastMoney;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.Locale;

public class LocalMoney {

    private static final Locale DEFAULT_LOCALE = new Locale("pl", "PL");

    public static FastMoney zero() {
        return FastMoney.zero(getCurrencyUnit());
    }

    public static FastMoney of(Number number) {
        return FastMoney.of(number, getCurrencyUnit());
    }

    private static CurrencyUnit getCurrencyUnit() {
        return Monetary.getCurrency(DEFAULT_LOCALE);
    }

}
