package com.company.core.services;

import com.company.core.BankNotesCell;
import com.company.core.exceptions.AtmException;
import com.company.core.models.BankNote;
import com.company.core.models.BankNoteDenomination;

import java.math.BigDecimal;
import java.util.*;

public class BankNotesManagerImpl implements BankNotesManager {
    private final Map<BankNoteDenomination, BankNotesCell> bankNotesCells = new HashMap<>();

    public BankNotesManagerImpl(Set<BankNoteDenomination> uniqueBankNoteDenominations) {
        uniqueBankNoteDenominations.forEach(bankNoteDenomination -> {
            bankNotesCells.put(bankNoteDenomination, new BankNotesCell());
        });
    }

    @Override
    public void put(List<BankNote> bankNotes) {
        bankNotes.forEach(
                bankNote -> bankNotesCells.get(bankNote.getDenomination()).put(bankNote));
    }

    @Override
    public List<BankNote> take(BigDecimal moneyAmount) {
        if (!checkEnoughMoney(moneyAmount)) {
            throw new AtmException("not enough money for required amount");
        }

        return getBankNotesCombination(moneyAmount);
    }

    @Override
    public boolean checkBankNotesCombinationExistence(BigDecimal moneyAmount) {
        try {
            getBankNotesCounterOfDenomination(moneyAmount);
            return true;
        }
        catch (AtmException e) {
            return false;
        }
    }

    @Override
    public boolean checkDenominationSupport(BankNoteDenomination bankNoteDenomination) {
        return bankNotesCells.containsKey(bankNoteDenomination);
    }

    private boolean checkEnoughMoney(BigDecimal requiredMoneyAmount) {
        BigDecimal sum = BigDecimal.valueOf(0);
        for (BankNoteDenomination denomination : bankNotesCells.keySet()) {
            var bankNotesOfDenomination = bankNotesCells.get(denomination).getAllBankNotes();
            sum = sum.add(BigDecimal.valueOf(denomination.getAmount() * bankNotesOfDenomination.size()));
            if (sum.compareTo(requiredMoneyAmount) > 0) {
                return true;
            }
        }
        return false;
    }

    private List<BankNote> getBankNotesCombination(BigDecimal amount) {
        var bankNotesCounterByDenomination = getBankNotesCounterOfDenomination(amount);
        final List<BankNote> bankNotes = new LinkedList<>();
        bankNotesCounterByDenomination.keySet().forEach(denomination -> {
            for (int i = 0; i < bankNotesCounterByDenomination.get(denomination); i++) {
                bankNotes.add(bankNotesCells.get(denomination).take().get());
            }
        });

        return bankNotes;
    }

    private Map<BankNoteDenomination, Integer> getBankNotesCounterOfDenomination(BigDecimal amount) {
        BigDecimal commonSum = BigDecimal.valueOf(0);
        Map<BankNoteDenomination, Integer> counter = new HashMap<>();
        for (BankNoteDenomination denomination : bankNotesCells.keySet()) {
            BigDecimal sum = BigDecimal.valueOf(0);
            counter.put(denomination, 0);
            var bankNotesOfDenomination = bankNotesCells.get(denomination).getAllBankNotes();
            for (BankNote bankNote : bankNotesOfDenomination) {
                var tmpSum = commonSum.add(BigDecimal.valueOf(bankNote.getDenomination().getAmount()));
                if (tmpSum.add(commonSum).compareTo(amount) > 0) {
                    break;
                }
                sum = sum.add(tmpSum);
                counter.compute(denomination, (__, currentCount) -> currentCount + 1);
            }
            commonSum = commonSum.add(sum);
        }

        if (commonSum.compareTo(amount) != 0) {
            throw new AtmException("can't find banknotes combination for your sum");
        }

        return counter;
    }
}
