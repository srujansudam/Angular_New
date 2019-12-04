package com.cg.ibs.rm.service;

import java.math.BigInteger;

import com.cg.ibs.rm.exception.IBSExceptions;
import com.cg.ibs.rm.model.Customer;

public interface CustomerService {

	public boolean checkUciList(BigInteger uci) throws IBSExceptions;

	public String returnName(BigInteger uci);
	public BigInteger returnUCI(String userID) throws IBSExceptions;
	
	public Customer getCustomer(String userId) throws IBSExceptions;
}
