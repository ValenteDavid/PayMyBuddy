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
import javax.persistence.Table;

@Entity
@Table(name = "utilisateur")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "email", length = 254, nullable = false, unique = true)
	private String email;

	@Column(name = "mots_de_passe", length = 50, nullable = false)
	private String password;

	@Column(name = "nom", length = 100, nullable = false)
	private String name;

	@Column(name = "solde", precision = 15, scale = 2, nullable = false)
	private BigDecimal balance;

	@Column(name = "iban", length = 34)
	private String iban;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "liste_contact", joinColumns = {
			@JoinColumn(name = "id_utilisateur_contact", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "id_utilisateur_liste", nullable = false, updatable = false) })
	private Set<User> setUser = new HashSet<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "utilisateurByIdUtilisateurDebiteur")
	private Set<TransactionInternal> setTransactionInternal = new HashSet<>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "utilisateur")
	private Set<TransactionBanking> setTransactionBanking = new HashSet<>();

	public User() {
	}

	public User(String email, String password, String name, BigDecimal amount) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.balance = amount;
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

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public Set<User> getSetUser() {
		return setUser;
	}

	public void setSetUser(Set<User> setUser) {
		this.setUser = setUser;
	}

	public Set<TransactionInternal> getSetTransactionInternal() {
		return setTransactionInternal;
	}

	public void setSetTransactionInternal(Set<TransactionInternal> setTransactionInternal) {
		this.setTransactionInternal = setTransactionInternal;
	}

	public Set<TransactionBanking> getSetTransactionBanking() {
		return setTransactionBanking;
	}

	public void setSetTransactionBanking(Set<TransactionBanking> setTransactionBanking) {
		this.setTransactionBanking = setTransactionBanking;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", password=" + password + ", name=" + name + ", amount="
				+ balance + ", iban=" + iban;
	}

}
