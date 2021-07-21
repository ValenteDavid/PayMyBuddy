package com.paymybuddy.paymybuddy.service;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.paymybuddy.paymybuddy.model.User;

@Service
public interface UserManagementConnexionService {

	public User registration(User user);

	public Optional<User> login(@Valid User user);

	public User findById(Integer id);

	public String addConnection(User user, String emailConnection);
}