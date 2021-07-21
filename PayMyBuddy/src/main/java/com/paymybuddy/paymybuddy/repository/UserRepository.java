package com.paymybuddy.paymybuddy.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.paymybuddy.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

	public Optional<User> findByEmail(String email);
	public Optional<User> findByEmailAndPassword(String email,String password);
	
}
