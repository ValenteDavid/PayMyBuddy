package com.paymybuddy.paymybuddy.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.paymybuddy.model.TransactionInternal;

@Repository
public interface TransactionInternalRepository extends CrudRepository<TransactionInternal, Integer>{

}
