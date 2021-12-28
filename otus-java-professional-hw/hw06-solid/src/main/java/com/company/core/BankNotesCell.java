package com.company.core;

import com.company.core.models.BankNote;

import java.util.*;

public class BankNotesCell {
    private final Deque<BankNote> bankNotes = new LinkedList<BankNote>();

    public Optional<BankNote> take() {
        try {
            return Optional.of(bankNotes.pop());
        }
        catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    public void put(BankNote bankNote) {
        bankNotes.add(bankNote);
    }

    public List<BankNote> getAllBankNotes() {
        return bankNotes.stream().toList();
    }
}