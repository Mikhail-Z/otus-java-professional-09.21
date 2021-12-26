package com.company.core.providers;

import java.math.BigDecimal;

public interface BankProvider {
    void deposit(String accountNumber, BigDecimal moneyAmount) throws Exception;
    void passOff(String accountNumber, BigDecimal moneyAmount) throws Exception;
    BigDecimal checkBalance(String accountNumber) throws Exception;
}
