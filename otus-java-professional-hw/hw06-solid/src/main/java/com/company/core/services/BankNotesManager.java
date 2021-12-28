package com.company.core.services;

import com.company.core.models.BankNote;
import com.company.core.models.BankNoteDenomination;

import java.math.BigDecimal;
import java.util.List;

public interface BankNotesManager {
    void put(List<BankNote> bankNotes);
    List<BankNote> take(BigDecimal moneyAmount);
    boolean checkBankNotesCombinationExistence(BigDecimal moneyAmount);
    boolean checkDenominationSupport(BankNoteDenomination bankNoteDenomination);
}
