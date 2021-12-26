package com.company.core.providers;

import java.math.BigDecimal;

public interface Printer {
    void printErrorMessage(String msg);
    void printBalance(BigDecimal balance);
    void printInfo(String msg);
}
