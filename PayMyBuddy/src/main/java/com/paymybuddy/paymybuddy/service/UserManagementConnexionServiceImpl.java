package com.paymybuddy.paymybuddy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.UserRepository;

@Service
public class UserManagementConnexionServiceImpl implements UserManagementConnexionService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	@Transactional
	public User registration(String loginEmail, String password,String name) {
		if(userRepository.findByEmailAndName(loginEmail, name).isEmpty()) {
			return userRepository.save(new User(loginEmail,password,name));
		}
		else {
			return null;
		}
	}

}
