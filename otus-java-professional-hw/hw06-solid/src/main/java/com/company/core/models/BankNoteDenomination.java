package com.company.core.models;

public enum BankNoteDenomination {
    DENOMINATION_50(50),
    DENOMINATION_100(100),
    DENOMINATION_500(500),
    DENOMINATION_1000(1000),
    DENOMINATION_5000(5000);

    private final int amount;

    private BankNoteDenomination(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
