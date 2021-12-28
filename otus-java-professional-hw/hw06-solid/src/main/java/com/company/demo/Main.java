package com.company.demo;

import com.company.core.ATM;
import com.company.core.models.BankNote;
import com.company.core.models.BankNoteDenomination;
import com.company.core.providers.DefaultPrinter;
import com.company.core.providers.Printer;
import com.company.core.providers.TestBankProvider;
import com.company.core.providers.factories.BankProviderFactory;
import com.company.core.providers.factories.TestBankProviderFactory;
import com.company.core.services.BankNotesManager;
import com.company.core.services.BankNotesManagerImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Printer printer = new DefaultPrinter();
        BankNotesManager bankNotesManager = new BankNotesManagerImpl(Set.of(new BankNoteDenomination[]{
                BankNoteDenomination.DENOMINATION_50,
                BankNoteDenomination.DENOMINATION_100,
                BankNoteDenomination.DENOMINATION_500,
                BankNoteDenomination.DENOMINATION_1000,
                BankNoteDenomination.DENOMINATION_5000
        }));
        BankProviderFactory factory = new TestBankProviderFactory(Set.of(new TestBankProvider()));
        ATM atm = new ATM(bankNotesManager, factory, printer);
        String account = "some account";
        atm.deposit(account, List.of(
                new BankNote(BankNoteDenomination.DENOMINATION_50),
                new BankNote(BankNoteDenomination.DENOMINATION_100)));
        atm.passOff(account, BigDecimal.valueOf(100));
        atm.checkBalance(account);
    }
}
