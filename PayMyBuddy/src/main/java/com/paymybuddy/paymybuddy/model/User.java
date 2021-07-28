package com.paymybuddy.paymybuddy.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "utilisateur")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Email
	@Length(max = 254)
	@NotNull(message = "Email cannot be null")
	@Column(name = "email", length = 254, nullable = false, unique = true)
	private String email;

	@Length(max = 50)
	@Column(name = "mots_de_passe", length = 50, nullable = false)
	private String password;

	@Length(max = 100)
	@NotNull(message = "Name cannot be null")
	@Column(name = "nom", length = 100, nullable = false)
	private String name;

	@Column(name = "solde", precision = 15, scale = 2, nullable = false)
	private BigDecimal balance;

	@Length(min = 34, max = 34)
	@Column(name = "iban", length = 34)
	private String iban;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "liste_contact", joinColumns = {
			@JoinColumn(name = "id_utilisateur_liste", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "id_utilisateur_contact", nullable = false, updatable = false) })
	private Set<User> contacts = new HashSet<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userDebtor")
	@OrderBy("id DESC")
	private Set<TransactionInternal> transactionInternals = new HashSet<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	@OrderBy("id DESC")
	private Set<TransactionBanking> transactionBankings = new HashSet<>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userCreditor")
	@OrderBy("id DESC")
	private Set<TransactionInternal> transactionInternalsReceived = new HashSet<>();

	public User() {
		this.balance = BigDecimal.ZERO;
	}

	public User(String email, String password, String name) {
		this();
		this.email = email;
		this.password = password;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal amount) {
		this.balance = amount;
	}
	
	public void addAmount(BigDecimal amount) {
		this.balance = balance.add(amount);
	}
	
	public void removeAmount(BigDecimal amount) {
		this.balance = balance.subtract(amount);
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public Set<User> getContacts() {
		return contacts;
	}

	public void setContacts(Set<User> setUser) {
		this.contacts = setUser;
	}

	public Set<TransactionInternal> getTransactionInternals() {
		return transactionInternals;
	}

	public void setTransactionInternals(Set<TransactionInternal> setTransactionInternal) {
		this.transactionInternals = setTransactionInternal;
	}

	public Set<TransactionBanking> getTransactionBankings() {
		return transactionBankings;
	}

	public void setTransactionBankings(Set<TransactionBanking> setTransactionBanking) {
		this.transactionBankings = setTransactionBanking;
	}
	
	public Set<TransactionInternal> getTransactionInternalsReceived() {
		return transactionInternalsReceived;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", password=" + password + ", name=" + name + ", balance="
				+ balance + ", iban=" + iban + ", setUser=" + contacts + "]";
	}
}
