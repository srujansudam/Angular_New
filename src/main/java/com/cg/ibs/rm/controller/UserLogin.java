package com.cg.ibs.rm.controller;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.ibs.rm.exception.IBSExceptions;
import com.cg.ibs.rm.service.CustomerService;

@RestController
@RequestMapping("/login")
@Scope("session")
@CrossOrigin
public class UserLogin {

	private BigInteger uci;
	@Autowired
	CustomerService customerService;

	public BigInteger getUci() {
		return uci;
	}

	public void setUci(BigInteger uci) {
		this.uci = uci;
	}

	@GetMapping("/{userId}")
	public ResponseEntity<String> getName(@PathVariable("userId") String userId) throws IBSExceptions {
		ResponseEntity<String> result;
	
			if (userId == null) {
				result = new ResponseEntity<>("No User Details Received", HttpStatus.BAD_REQUEST);
			} else {
				uci = customerService.returnUCI(userId);
				result = new ResponseEntity<>(
						"welcome " + customerService.returnName(uci), HttpStatus.OK);
			}
		
		return result;
	}
	
	@ExceptionHandler(IBSExceptions.class)
	public ResponseEntity<String> handleAdbException(IBSExceptions exp) {
		return new ResponseEntity<String>(exp.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleException(Exception exp) {
		return new ResponseEntity<String>(exp.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}

	