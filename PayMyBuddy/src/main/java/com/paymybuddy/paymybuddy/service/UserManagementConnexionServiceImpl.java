package com.paymybuddy.paymybuddy.service;

import java.util.Optional;

import javax.validation.Valid;

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
	public User registration(User user) {
		if (userRepository.findByEmail(user.getEmail()).isEmpty()) {
			return userRepository.save(user);
		} else {
			return null;
		}
	}

	@Override
	@Transactional
	public Optional<User> login(@Valid User user) {
		return userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
	}

	@Override
	@Transactional
	public User findById(Integer id) {
		Optional<User> user = userRepository.findById(id);
		if (user.isPresent()) {
			return user.get();
		} else {
			return null;
		}
	}

	@Override
	@Transactional
	public String addConnection(User user, String emailConnection) {
		Optional<User> userContact = userRepository.findByEmail(emailConnection);
		if (userContact.isPresent()) {
			user.getContacts().add(userContact.get());
			return emailConnection;
		} else {
			return null;
		}
	}

}
