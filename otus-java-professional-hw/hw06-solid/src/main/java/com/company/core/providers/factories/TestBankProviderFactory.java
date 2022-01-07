package com.company.core.providers.factories;

import com.company.core.providers.BankProvider;

import java.util.Optional;
import java.util.Set;

public class TestBankProviderFactory implements BankProviderFactory {
    private final Set<BankProvider> bankProviders;

    public TestBankProviderFactory(Set<BankProvider> bankProviders) {
        this.bankProviders = bankProviders;
    }

    @Override
    public Optional<BankProvider> getBankProvider(String accountNumber) {
        return bankProviders.stream().filter(this::condition).findFirst();
    }

    private boolean condition(BankProvider bankProvider) {
        //some logic of defining bank by account number
        return true;
    }
}
