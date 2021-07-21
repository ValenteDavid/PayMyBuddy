package com.paymybuddy.paymybuddy.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.paymybuddy.paymybuddy.PayMyBuddyApplication;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.TransactionInternalRepository;
import com.paymybuddy.paymybuddy.repository.UserRepository;

@SpringBootTest(classes = PayMyBuddyApplication.class)
class UserManagementConnexionServiceImplTest {
	
	private User user;

	@Autowired
	private UserManagementConnexionService userManagementConnexionService;
	
	@MockBean 
	private TransactionInternalRepository transactionInternalRepository;

	@MockBean
	private UserRepository userRepository;
	
	@BeforeEach
	void setUp() {
		String loginEmail = "login@login.com";
		String password = "password";
		String name = "name";

		user = new User(loginEmail, password, name);
	}
	

	@Test
	void testRegistration_ValidArguments() {
		when(userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(null);
		when(userRepository.save(user)).thenReturn(user);

		User userResult = userManagementConnexionService.registration(user);

		assertNotNull(userResult);
	}

	@Test
	void testRegistration_ExistEmail_ReturnNull() {
		when(userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(Optional.of(user));
		
		User userResult = userManagementConnexionService.registration(user);

		assertNull(userResult);
	}
	
	@Test
	void testLogin(){
		when(userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(Optional.of(user));
		Optional<User> opt = userManagementConnexionService.login(user);
		
		assertTrue(opt.isPresent());
		
		verify(userRepository, times(1)).findByEmailAndPassword(any(),any());
	}
	
	@Test
	void  testfindById_IdFound(){
		Integer id = 100;
		when(userRepository.findById(id)).thenReturn(Optional.of(user));
		User result = userManagementConnexionService.findById(id);
		
		assertNotNull(result);
	}
	
	@Test
	void testfindById_IdNotFound(){
		Integer id = 100;
		when(userRepository.findById(id)).thenReturn(Optional.ofNullable(null));
		User result = userManagementConnexionService.findById(id);
		
		assertNull(result);
	}

	@Test
	void testaddConnection() {
		User user = new User();
		String emailConnection = "email@email.com";
		
		when(userRepository.findByEmail(emailConnection)).thenReturn(Optional.of(user));
		String result = userManagementConnexionService.addConnection(user,emailConnection);
		
		assertEquals(emailConnection,result);
	}
	

	@Test
	void testGaddConnection_ConnectionNotFound() {
		User user = new User();
		String emailConnection = "email@email.com";
		
		when(userRepository.findByEmail(emailConnection)).thenReturn(Optional.ofNullable(null));
		String result = userManagementConnexionService.addConnection(user,emailConnection);
		
		assertNull(result);
	}
}
