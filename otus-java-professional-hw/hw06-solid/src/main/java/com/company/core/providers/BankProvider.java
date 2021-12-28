package com.company.core.providers;

import java.math.BigDecimal;

public interface BankProvider {
    void deposit(String accountNumber, BigDecimal moneyAmount);
    void passOff(String accountNumber, BigDecimal moneyAmount);
    BigDecimal checkBalance(String accountNumber);
}
