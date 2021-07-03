package com.paymybuddy.paymybuddy.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.paymybuddy.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

}