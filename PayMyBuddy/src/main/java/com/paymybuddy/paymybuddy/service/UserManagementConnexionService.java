package com.paymybuddy.paymybuddy.service;

import org.springframework.stereotype.Service;

import com.paymybuddy.paymybuddy.model.User;

@Service
public interface UserManagementConnexionService {
	
	public User registration(String loginEmail, String password, String name);

}
