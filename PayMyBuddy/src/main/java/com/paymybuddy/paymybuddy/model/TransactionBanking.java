package com.paymybuddy.paymybuddy.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;

@Entity
@Table(name = "transaction_bancaire")
public class TransactionBanking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_utilisateur", nullable = false)
	private User user;

	@Min(value = (long) 0.01)
	@Column(name = "montant", precision = 15, scale = 2, nullable = false)
	private BigDecimal amount;

	@Override
	public String toString() {
		return "TransactionBanking [id=" + id + ", user=" + user + ", amount=" + amount + "]";
	}

}
