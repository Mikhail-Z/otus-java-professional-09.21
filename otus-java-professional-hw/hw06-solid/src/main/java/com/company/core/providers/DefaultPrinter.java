package com.company.core.providers;

import java.math.BigDecimal;

public class DefaultPrinter implements Printer {
    @Override
    public void printErrorMessage(String msg) {
        System.out.println(msg);
    }

    @Override
    public void printBalance(BigDecimal balance) {
        System.out.println(balance);
    }

    @Override
    public void printInfo(String msg) {
        System.out.println(msg);
    }
}
