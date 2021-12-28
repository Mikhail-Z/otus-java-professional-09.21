package com.company.core.models;

public class BankNote {
    private final BankNoteDenomination sum;

    public BankNote(BankNoteDenomination sum) {
        this.sum = sum;
    }

    public BankNoteDenomination getDenomination() {
        return sum;
    }
}
