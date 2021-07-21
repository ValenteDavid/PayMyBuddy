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

@Entity
@Table(name = "transaction_bancaire")
public class TransactionBanking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private String id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_utilisateur", nullable = false)
	private User user;

	@Column(name = "montant", precision = 15, scale = 2, nullable = false)
	private BigDecimal amount;
	
	public TransactionBanking() {
		super();
	}

	public TransactionBanking(User user, BigDecimal amount) {
		super();
		this.user = user;
		this.amount = amount;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	@Override
	public String toString() {
		return "TransactionBanking [id=" + id + ", user=" + user + ", amount=" + amount + "]";
	}
}
