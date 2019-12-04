package com.cg.ibs.rm.model;

public class Message {

	private String message;
	private Customer customer;
	private Banker banker;

	public Message() {
		super();
	}

	public Message(String message, Customer customer, Banker banker) {
		super();
		this.message = message;
		this.customer = customer;
		this.banker = banker;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Banker getBanker() {
		return banker;
	}

	public void setBanker(Banker banker) {
		this.banker = banker;
	}

}
