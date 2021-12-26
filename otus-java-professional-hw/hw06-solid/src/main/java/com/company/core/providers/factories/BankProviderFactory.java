package com.company.core.providers.factories;

import com.company.core.providers.BankProvider;

import java.util.Optional;

public interface BankProviderFactory {
    Optional<BankProvider> getBankProvider(String accountNumber);
}
