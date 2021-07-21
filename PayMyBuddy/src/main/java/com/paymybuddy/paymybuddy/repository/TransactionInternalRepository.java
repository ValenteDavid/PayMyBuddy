package com.paymybuddy.paymybuddy.repository;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.paymybuddy.model.TransactionInternal;
import com.paymybuddy.paymybuddy.model.User;

@Repository
public interface TransactionInternalRepository extends CrudRepository<TransactionInternal, Integer> {

	Set<TransactionInternal> findByUserDebtor(User user);

}
