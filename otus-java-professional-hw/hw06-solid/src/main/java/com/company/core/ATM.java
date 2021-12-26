package com.company.core;

import com.company.core.models.BankNote;
import com.company.core.models.BankNoteDenomination;
import com.company.core.providers.Printer;
import com.company.core.providers.factories.BankProviderFactory;
import com.company.core.services.BankNotesManager;

import java.math.BigDecimal;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

public class ATM {

    private final BankNotesManager bankNotesManager;
    private final BankProviderFactory bankProviderFactory;
    private final Printer printer;

    public ATM(BankNotesManager bankNotesManager, BankProviderFactory bankProviderFactory, Printer printer) {
        this.bankNotesManager = bankNotesManager;
        this.bankProviderFactory = bankProviderFactory;
        this.printer = printer;
    }

    public void deposit(String accountNumber, List<BankNote> bankNotes) {
        if (!checkDenominationSupport(bankNotes)) {
            printer.printErrorMessage("Denomination is not supported");
            return;
        }

        var amount = getFullMoneyAmount(bankNotes);
        var bankProviderOptional = bankProviderFactory.getBankProvider(accountNumber);
        if (bankProviderOptional.isEmpty()) {
            printer.printErrorMessage("bank provider by account number not found");
            return;
        }

        try {
            bankProviderOptional.get().deposit(accountNumber, amount);
        } catch (Exception e) {
            printer.printErrorMessage(e.getMessage());
            return;
        }

        bankNotesManager.put(bankNotes);
        printer.printInfo("OK");
    }

    public void passOff(String accountNumber, BigDecimal moneyAmount) {
        var bankProviderOptional = bankProviderFactory.getBankProvider(accountNumber);
        if (bankProviderOptional.isEmpty()) {
            printer.printErrorMessage("bank provider by account number not found");
            return;
        }

        if (!bankNotesManager.checkBankNotesCombinationExistence(moneyAmount)) {
            printer.printErrorMessage("can't find banknotes combination for your sum");
        }
        try {
            bankProviderOptional.get().passOff(accountNumber, moneyAmount);
        } catch (Exception e) {
            printer.printErrorMessage(e.getMessage());
            return;
        }

        List<BankNote> bankNotes = null;
        try {
            bankNotes = bankNotesManager.take(moneyAmount);
        } catch (Exception e) {
            printer.printErrorMessage(e.getMessage());
            return;
        }

        giveOutMoney(bankNotes);
        printer.printInfo("OK");
    }

    public void checkBalance(String accountNumber) {
        var bankProviderOptional = bankProviderFactory.getBankProvider(accountNumber);
        if (bankProviderOptional.isEmpty()) {
            printer.printErrorMessage("bank provider by account number not found");
            return;
        }

        try {
            var balance = bankProviderOptional.get().checkBalance(accountNumber);
            printer.printBalance(balance);
        } catch (Exception e) {
            printer.printErrorMessage(e.getMessage());
        }
    }

    private BigDecimal getFullMoneyAmount(List<BankNote> bankNotes) {
        var sum = bankNotes.stream().mapToInt(bankNote -> bankNote.getDenomination().getAmount()).sum();
        return BigDecimal.valueOf(sum);
    }

    private boolean checkDenominationSupport(List<BankNote> bankNotes) {
        for (Map.Entry<BankNoteDenomination, List<BankNote>> entry : bankNotes.stream()
                .collect(groupingBy(BankNote::getDenomination)).entrySet()) {
            BankNoteDenomination denomination = entry.getKey();
            List<BankNote> __ = entry.getValue();
            if (!bankNotesManager.checkDenominationSupport(denomination)) {
                return false;
            }
        }

        return true;
    }

    private void giveOutMoney(List<BankNote> bankNotes) {
        //some logic
    }
}
