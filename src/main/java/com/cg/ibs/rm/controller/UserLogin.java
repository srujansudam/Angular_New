package com.cg.ibs.rm.controller;

import java.math.BigInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.ibs.rm.exception.IBSExceptions;
import com.cg.ibs.rm.model.Message;
import com.cg.ibs.rm.service.CreditCardService;
import com.cg.ibs.rm.service.CustomerService;

@RestController
@RequestMapping("/login")
@Scope("session")
@CrossOrigin
public class UserLogin {

	private BigInteger uci;
	@Autowired
	CustomerService customerService;
	@Autowired
	CreditCardService creditCard;
	public BigInteger getUci() {
		return uci;
	}

	public void setUci(BigInteger uci) {
		this.uci = uci;
		
		
	}

	@GetMapping("/{userId}")
	public ResponseEntity<Message> getName(@PathVariable("userId") String userId) {
		ResponseEntity<Message> result;
		try {
			
			result = new ResponseEntity<>(
					new Message("Logged in succesfully!!", customerService.getCustomer(userId), null), HttpStatus.OK);
			uci = customerService.getCustomer(userId).getUci();
		} catch (IBSExceptions e) {
			result = new ResponseEntity<>(new Message(e.getMessage(), null, null), HttpStatus.BAD_REQUEST);
		}

		return result;
	}

}
