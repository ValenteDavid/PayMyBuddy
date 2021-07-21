package com.paymybuddy.paymybuddy.service;

import java.math.BigDecimal;

import com.paymybuddy.paymybuddy.model.TransactionBanking;
import com.paymybuddy.paymybuddy.model.TransactionInternal;
import com.paymybuddy.paymybuddy.model.User;

public interface TransactionService {

	TransactionInternal addTransactionInternal(User user, Integer contactID, String description, BigDecimal amount);

	TransactionBanking addTransactionBankingIn(User user, BigDecimal amount);
	TransactionBanking addTransactionBankingOut(User user, BigDecimal amount);

}
