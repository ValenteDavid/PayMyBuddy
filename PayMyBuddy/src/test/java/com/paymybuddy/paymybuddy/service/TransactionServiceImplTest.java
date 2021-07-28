package com.paymybuddy.paymybuddy.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.paymybuddy.paymybuddy.PayMyBuddyApplication;
import com.paymybuddy.paymybuddy.model.TransactionBanking;
import com.paymybuddy.paymybuddy.model.TransactionInternal;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.TransactionBankingRepository;
import com.paymybuddy.paymybuddy.repository.TransactionInternalRepository;
import com.paymybuddy.paymybuddy.repository.UserRepository;

@SpringBootTest(classes = PayMyBuddyApplication.class)
class TransactionServiceImplTest {

	@Autowired
	private TransactionService transactionService;

	private User userDebtor;
	private User userCreditor;
	private TransactionInternal transactionInternal;

	@MockBean
	private UserRepository userRepository;
	@MockBean
	private TransactionInternalRepository transactionInternalRepository;
	@MockBean
	private BillingService billingService;
	@MockBean
	private TransactionBankingRepository transactionBankingRepository;

	@BeforeEach
	void init() {
		userDebtor = new User();
		userCreditor = new User();
		transactionInternal = new TransactionInternal();
	}

	@Test
	void testAddTransactionInternal_ValidArgument() {
		Integer contactID = 8;
		String description = "";
		BigDecimal amount = new BigDecimal(100);
		userDebtor.setBalance(new BigDecimal(500));

		when(userRepository.findById(contactID)).thenReturn(Optional.of(userCreditor));
		when(transactionInternalRepository.save(transactionInternal)).thenReturn(transactionInternal);
		doNothing().when(billingService).addBilling(any(TransactionInternal.class));

		TransactionInternal transactionInternalSave = transactionService.addTransactionInternal(userDebtor, contactID,
				description, amount);

		assertEquals(new BigDecimal(400), userDebtor.getBalance());
		verify(userRepository, times(1)).save(userDebtor);

		assertEquals(new BigDecimal(100), userCreditor.getBalance());
		verify(userRepository, times(1)).save(userCreditor);

		TransactionInternal transactionInternalExpect = new TransactionInternal(amount, description);
		transactionInternalExpect.setUserDebtor(userDebtor);
		transactionInternalExpect.setUserCreditor(userCreditor);

		assertTrue(new ReflectionEquals(transactionInternalExpect).matches(transactionInternalSave));
	}

	@Test
	void testAddTransactionInternal_InvalidAmount() {
		Integer contactID = 8;
		String description = "";
		BigDecimal amount = new BigDecimal(-100);

		TransactionInternal transactionInternalSave = transactionService.addTransactionInternal(userDebtor, contactID,
				description,
				amount);

		assertNull(transactionInternalSave);
	}

	@Test
	void testAddTransactionInternal_InsufficientBalance() {
		Integer contactID = 8;
		String description = "";
		BigDecimal amount = new BigDecimal(100);
		userDebtor.setBalance(new BigDecimal(99));

		TransactionInternal transactionInternalSave = transactionService.addTransactionInternal(userDebtor, contactID,
				description,
				amount);

		assertNull(transactionInternalSave);
	}

	@Test
	void testAddTransactionBankingIn() {
		BigDecimal amount = new BigDecimal(100);
		userCreditor.setBalance(new BigDecimal(500));
		
		when(userRepository.save(any())).thenReturn(userCreditor);
		when(transactionBankingRepository.save(any())).thenReturn(new TransactionBanking(userCreditor, amount));

		TransactionBanking transactionBankingSave = transactionService.addTransactionBankingIn(userCreditor, amount);
		
		assertNotNull(transactionBankingSave);
		
		assertEquals(new BigDecimal(600),userCreditor.getBalance());
		verify(userRepository, times(1)).save(any());
		verify(transactionBankingRepository, times(1)).save(any());
	}

	@Test
	void testAddTransactionBankingOut() {
		userDebtor.setBalance(new BigDecimal(500));

		BigDecimal amount = new BigDecimal(100);

		when(userRepository.save(any())).thenReturn(userDebtor);
		when(transactionBankingRepository.save(any())).thenReturn(new TransactionBanking(userCreditor, amount));

		TransactionBanking transactionBankingSave = transactionService.addTransactionBankingOut(userDebtor, amount);
		
		assertNotNull(transactionBankingSave);

		assertEquals(new BigDecimal(400),userDebtor.getBalance());
		verify(userRepository, times(1)).save(any());
		verify(transactionBankingRepository, times(1)).save(any());
	}

}
