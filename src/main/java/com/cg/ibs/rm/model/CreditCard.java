package com.cg.ibs.rm.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cg.ibs.rm.ui.CardStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Credit_Cards")
public class CreditCard {

	@Id
	@Column(nullable = false, length = 16, name = "credit_card_number")
	private BigInteger cardNumber;
	@Column(nullable = false, length = 3, name = "credit_score")
	private Integer creditScore;
	@Column(nullable = false, name = "credit_limit")
	private BigDecimal creditLimit;
	@Column(nullable = false, name = "customer_income")
	private Double income;
	@Column(name = "credit_card_Status", nullable = false)
	@Enumerated(EnumType.STRING)
	private CardStatus cardStatus = CardStatus.PENDING;
	@Column(name = "name_On_Credit_Card", nullable = false)
	private String nameOnCard;
	@Column(name = "Credit_Cvv", nullable = false, length = 3)
	private String cvvNum;
	@Column(name = "Credit_Pin", nullable = false, length = 4)
	private String currentPin;
	@Column(name = "Credit_Card_Type", nullable = false)
	private String cardType;
	@Transient
	private int month;
	@Transient
	private int year;
	@Column(name = "Credit_Expiry_Date", nullable = false)
	private LocalDate dateOfExpiry;
	@Column(name = "Credit_Card_Timestamp")
	private LocalDateTime timestamp;
	@Column(name = "BANKER_ID", nullable = false)
	private Integer bankId;
	@Column(name = "Remarks")
	private String adminRemarks = " ";

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "UCI")
	private Customer customer;
	
	public Integer getBankId() {
		return bankId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}



	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getAdminRemarks() {
		return adminRemarks;
	}

	public void setAdminRemarks(String adminRemarks) {
		this.adminRemarks = adminRemarks;
	}

	public CreditCard() {
		super();
	}

	public BigInteger getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(BigInteger cardNumber) {
		this.cardNumber = cardNumber;
	}

	public CardStatus getCardStatus() {
		return cardStatus;
	}

	public void setCardStatus(CardStatus cardStatus) {
		this.cardStatus = cardStatus;
	}

	public String getNameOnCard() {
		return nameOnCard;
	}

	public void setNameOnCard(String nameOnCard) {
		this.nameOnCard = nameOnCard;
	}

	public String getCvvNum() {
		return cvvNum;
	}

	public void setCvvNum(String cvvNum) {
		this.cvvNum = cvvNum;
	}

	public String getCurrentPin() {
		return currentPin;
	}

	public void setCurrentPin(String currentPin) {
		this.currentPin = currentPin;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public LocalDate getDateOfExpiry() {
		return dateOfExpiry;
	}

	public void setDateOfExpiry(LocalDate dateOfExpiry) {
		this.dateOfExpiry = dateOfExpiry;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

//	public Status getStatus() {
//		return status;
//	}
//
//	public void setStatus(Status status) {
//		this.status = status;
//	}

	public Integer getCreditScore() {
		return creditScore;
	}

	

	public CreditCard(BigInteger cardNumber, Integer creditScore, BigDecimal creditLimit, Double income,
		CardStatus cardStatus, String nameOnCard, String cvvNum, String currentPin, String cardType, int month,
		int year, LocalDate dateOfExpiry, LocalDateTime timestamp, Integer bankId, String adminRemarks,
		Customer customer) {
	super();
	this.cardNumber = cardNumber;
	this.creditScore = creditScore;
	this.creditLimit = creditLimit;
	this.income = income;
	this.cardStatus = cardStatus;
	this.nameOnCard = nameOnCard;
	this.cvvNum = cvvNum;
	this.currentPin = currentPin;
	this.cardType = cardType;
	this.month = month;
	this.year = year;
	this.dateOfExpiry = dateOfExpiry;
	this.timestamp = timestamp;
	this.bankId = bankId;
	this.adminRemarks = adminRemarks;
	this.customer = customer;
}

	public void setCreditScore(Integer creditScore) {
		this.creditScore = creditScore;
	}

	public BigDecimal getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(BigDecimal creditLimit) {
		this.creditLimit = creditLimit;
	}

	public Double getIncome() {
		return income;
	}

	public void setIncome(Double income) {
		this.income = income;
	}


}