package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.paymybuddy.PayMyBuddyApplication;
import com.paymybuddy.paymybuddy.model.TransactionInternal;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.TransactionInternalRepository;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import com.paymybuddy.paymybuddy.service.TransactionService;

@SpringBootTest(classes = PayMyBuddyApplication.class)
class TransactionServiceTest {

	@Autowired
	private TransactionService transactionService;

	private User userDebtor;
	private User userCreditor;
	private TransactionInternal transactionInternal;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TransactionInternalRepository transactionInternalRepository;

	@BeforeEach
	void init() {
		User userD = new User("zzz@email.com", "mdp", "zzz");
		userD.setBalance(new BigDecimal(1000));
		userDebtor = userRepository.save(userD);

		User userC = new User("yyy@email.com", "mdp", "yyy");
		userC.setBalance(new BigDecimal(1000));
		userCreditor = userRepository.save(userC);
	}

	@AfterEach
	void close() {
		transactionInternalRepository.delete(transactionInternal);
		userRepository.delete(userDebtor);
		userRepository.delete(userCreditor);
	}

	@Test
	@Transactional
	void testAddTransactionInternal_ValidArgument() {
		String description = "description";
		BigDecimal amount = new BigDecimal(100).setScale(2);

		TransactionInternal transactionInternalSave = transactionService.addTransactionInternal(userDebtor, userCreditor.getId(),
				description, amount);

		assertEquals(new BigDecimal(899.50).setScale(2), userRepository.findById(userDebtor.getId()).get().getBalance());
		assertEquals(new BigDecimal(1100).setScale(2), userRepository.findById(userCreditor.getId()).get().getBalance());

		transactionInternal = transactionInternalRepository.findById(transactionInternalSave.getId()).get();

		assertTrue(new ReflectionEquals(transactionInternal).matches(transactionInternalSave));

		assertEquals(amount, transactionInternal.getAmount());
		assertEquals(userDebtor.getId(), transactionInternal.getUserDebtor().getId());
		assertEquals(userCreditor.getId(), transactionInternal.getUserCreditor().getId());
		assertEquals(description, transactionInternal.getDescription());
	}

}
