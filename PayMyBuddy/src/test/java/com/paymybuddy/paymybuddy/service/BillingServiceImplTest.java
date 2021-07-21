package com.paymybuddy.paymybuddy.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.paymybuddy.paymybuddy.PayMyBuddyApplication;
import com.paymybuddy.paymybuddy.model.TransactionInternal;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.UserRepository;

@SpringBootTest(classes = PayMyBuddyApplication.class)
class BillingServiceImplTest {

	@Autowired
	private BillingServiceImpl billingService;

	@MockBean
	private UserRepository userRepository;

	@Test
	void testAddBilling() {
		final BigDecimal amount = new BigDecimal(1000);

		TransactionInternal transactionInternal = new TransactionInternal();
		transactionInternal.setAmount(amount);

		User userDebtor = new User();
		userDebtor.setBalance(new BigDecimal(1000));
		User userCreditor = new User();
		transactionInternal.setUserDebtor(userDebtor);
		transactionInternal.setUserCreditor(userCreditor);

		billingService.addBilling(transactionInternal);

		BigDecimal balanceExpect = amount.subtract(amount.multiply(BillingServiceImpl.billingRate)).setScale(2,
				RoundingMode.DOWN);
		
		verify(userRepository, times(1)).save(userDebtor);

		assertEquals(balanceExpect, userDebtor.getBalance());
	}

}
