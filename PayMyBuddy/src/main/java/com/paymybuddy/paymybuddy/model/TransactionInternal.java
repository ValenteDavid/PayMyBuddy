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
@Table(name = "transaction_interne")
public class TransactionInternal {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_utilisateur_debiteur")
	private User userDebtor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_utilisateur_crediteur")
	private User userCreditor;

	@Column(name = "montant", precision = 15, scale = 2, nullable = false)
	private BigDecimal amount;

	@Column(name = "description", length = 50)
	private String description;

	public TransactionInternal() {
		super();
	}

	public TransactionInternal(BigDecimal amount, String description) {
		super();
		this.amount = amount;
		this.description = description;
	}

	public User getUserDebtor() {
		return userDebtor;
	}

	public void setUserDebtor(User userDebtor) {
		this.userDebtor = userDebtor;
	}

	public User getUserCreditor() {
		return userCreditor;
	}

	public void setUserCreditor(User userCreditor) {
		this.userCreditor = userCreditor;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Transaction [id=" + id + ", userDebtor=" + userDebtor + ", userCreditor=" + userCreditor + ", amount="
				+ amount + ", description=" + description + "]";
	}

}
