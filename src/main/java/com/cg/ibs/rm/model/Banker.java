package com.cg.ibs.rm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BANKERS")
public class Banker {
	@Id
	@Column(name="BANKER_ID")
	private Integer bankerId;
	@Column(name="USER_ID")
	private String userId;
	@Column(name="PASSWORD")
	private String password;
	
	public Banker() {
		super();
	}
	public Banker(Integer bankerId, String userId, String password) {
		super();
		this.bankerId = bankerId;
		this.userId = userId;
		this.password = password;
	}
	public Integer getBankerId() {
		return bankerId;
	}
	public void setBankerId(Integer bankerId) {
		this.bankerId = bankerId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	

}
