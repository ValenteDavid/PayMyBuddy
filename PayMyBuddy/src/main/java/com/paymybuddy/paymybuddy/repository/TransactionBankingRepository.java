package com.paymybuddy.paymybuddy.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.paymybuddy.model.TransactionBanking;

@Repository
public interface TransactionBankingRepository extends CrudRepository<TransactionBanking, Integer>{

}
