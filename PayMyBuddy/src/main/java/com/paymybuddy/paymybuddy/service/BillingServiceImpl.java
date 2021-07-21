package com.paymybuddy.paymybuddy.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.paymybuddy.model.TransactionInternal;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.UserRepository;

@Service
public class BillingServiceImpl implements BillingService {

	private static final Logger log = LoggerFactory.getLogger(BillingServiceImpl.class);

	public static final BigDecimal billingRate = BigDecimal.valueOf(0.5 / 100);

	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional
	public void addBilling(TransactionInternal transactionInternal) {
		log.info("Facturation Start-" +
				"transaction ID : " + transactionInternal.getId() + " " +
				"User Debtor ID :" + transactionInternal.getUserDebtor().getId() + ", " +
				"User Creditor ID :" + transactionInternal.getUserCreditor().getId() + ", " +
				"Amount :" + transactionInternal.getAmount());

		BigDecimal levy = transactionInternal.getAmount().multiply(billingRate).setScale(2, RoundingMode.DOWN);
		User user = transactionInternal.getUserDebtor();
		user.removeAmount(levy);
		userRepository.save(user);

		log.info("Facturation End- " +
				"transaction ID : " + transactionInternal.getId() + ", " +
				"User Debtor ID :" + transactionInternal.getUserDebtor().getId() + ", " +
				"User Creditor ID :" + transactionInternal.getUserCreditor().getId() + ", " +
				"Amount :" + transactionInternal.getAmount() + ", " +
				"BillingRate :" + billingRate + ", " +
				"levy : " + levy);
	}
}
