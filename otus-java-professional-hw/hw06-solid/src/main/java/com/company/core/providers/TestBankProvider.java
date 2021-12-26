package com.company.core.providers;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class TestBankProvider implements BankProvider {
    private Map<String, BigDecimal> map = new HashMap<>();

    public TestBankProvider() {
        map.put(TestData.ACCOUNT_NUMBER_1, TestData.MONEY_AMOUNT_1);
        map.put(TestData.ACCOUNT_NUMBER_2, TestData.MONEY_AMOUNT_2);
    }

    @Override
    public void deposit(String accountNumber, BigDecimal moneyAmount) {
        if (map.containsKey(accountNumber)) {
            map.computeIfPresent(accountNumber, (__, currentSum) -> currentSum.add(moneyAmount));
        } else {
            map.computeIfAbsent(accountNumber, accNum -> moneyAmount);
        }

    }

    @Override
    public void passOff(String accountNumber, BigDecimal moneyAmount) throws Exception {
        if (map.containsKey(accountNumber)) {
            if (map.get(accountNumber).compareTo(moneyAmount) < 0) {
                throw new Exception("not enough money on account");
            }

            map.computeIfPresent(accountNumber, (__, currentSum) -> currentSum.subtract(moneyAmount));
        } else {
            throw new Exception("no such account");
        }
    }

    @Override
    public BigDecimal checkBalance(String accountNumber) throws Exception {
        var balance = map.get(accountNumber);
        if (balance == null) {
            throw new Exception("no such account");
        }

        return balance;
    }

    public static class TestData {
        public static final String ACCOUNT_NUMBER_1 = "some account number 1";
        public static final String ACCOUNT_NUMBER_2 = "some account number 2";

        public static final BigDecimal MONEY_AMOUNT_1 = new BigDecimal(10000);
        public static final BigDecimal MONEY_AMOUNT_2 = new BigDecimal(20000);
    }
}
