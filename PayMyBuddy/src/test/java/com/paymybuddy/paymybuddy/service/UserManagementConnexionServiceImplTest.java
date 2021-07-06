package com.paymybuddy.paymybuddy.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.paymybuddy.paymybuddy.PayMyBuddyApplication;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.UserRepository;

@SpringBootTest(classes = PayMyBuddyApplication.class)
class UserManagementConnexionServiceImplTest {
	
	@Autowired
	private UserManagementConnexionService managementConnexionServiceImpl;
	
	@MockBean
	private UserRepository userRepository;

	@Test
	void connexionWithValidArguments() {
		String loginEmail = "login@login.com";
		String password = "password";
		String name = "name";
		
		User userToSave = new User(loginEmail,password,name);
		
		when(userRepository.findByEmailAndName(loginEmail,password)).thenReturn(null);
		when(userRepository.save(any())).thenReturn(userToSave);
		
		User userSave = managementConnexionServiceImpl.registration(loginEmail, password,name);
		
		assertNotNull(userSave);
	}
	
	@Test
	void connexionWithExistEmailThenReturnMessage() {
		String loginEmail = "login@login.com";
		String password = "password";
		String name = "name";
		
		Optional<User> userToSave = Optional.of(new User(loginEmail,password,name));
		
		when(userRepository.findByEmailAndName(loginEmail,password)).thenReturn(userToSave);
		User userSave = managementConnexionServiceImpl.registration(loginEmail, password,name);
		
		assertNull(userSave);
	}

}
