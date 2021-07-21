package com.paymybuddy.paymybuddy.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.paymybuddy.model.TransactionBanking;
import com.paymybuddy.paymybuddy.model.TransactionInternal;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.TransactionBankingRepository;
import com.paymybuddy.paymybuddy.repository.TransactionInternalRepository;
import com.paymybuddy.paymybuddy.repository.UserRepository;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TransactionInternalRepository transactionInternalRepository;
	
	@Autowired
	private BillingService billingService;
	
	@Autowired
	private TransactionBankingRepository transactionBankingRepository;
	

	@Override
	@Transactional
	public TransactionInternal addTransactionInternal(User user, Integer contactID, String description,
			BigDecimal amount) {
		if (amount.compareTo(BigDecimal.ZERO) >= 0) {
			if (user.getBalance().compareTo(amount)>=0) {
				
				user.removeAmount(amount);
				userRepository.save(user);
				
				User userCreditor =	userRepository.findById(contactID).get();
				userCreditor.addAmount(amount);
				userRepository.save(userCreditor);
				
				TransactionInternal transactionInternal = new TransactionInternal();
				transactionInternal.setUserDebtor(user);
				transactionInternal.setUserCreditor(userCreditor);
				transactionInternal.setDescription(description);
				transactionInternal.setAmount(amount);
				transactionInternalRepository.save(transactionInternal);
				
				billingService.addBilling(transactionInternal);
				
				return transactionInternal;
			}
		}
		return null;
	}

	@Override
	@Transactional
	public TransactionBanking addTransactionBankingIn(User user, BigDecimal amount) {
		user.addAmount(amount);
		userRepository.save(user);
		return addTransactionBanking(user, amount);
	}
	
	@Override
	@Transactional
	public TransactionBanking addTransactionBankingOut(User user, BigDecimal amount) {
		user.removeAmount(amount);
		userRepository.save(user);
		return addTransactionBanking(user, amount.negate());
	}
	
	@Transactional
	private TransactionBanking addTransactionBanking(User user, BigDecimal amount) {
		TransactionBanking transactionBanking = new TransactionBanking(user,amount);
		return transactionBankingRepository.save(transactionBanking);
	}
	
}
