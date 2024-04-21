package org.example.corebanking.domain.enums;

public enum Currency {
    EUR, SEK, GBP, USD;

    public static boolean isValidCurrency(String currencyCode) {
        try {
            Currency.valueOf(currencyCode);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
