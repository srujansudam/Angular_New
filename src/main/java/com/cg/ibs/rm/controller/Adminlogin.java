package com.cg.ibs.rm.controller;

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
import com.cg.ibs.rm.model.Banker;
import com.cg.ibs.rm.service.Bank_AdminService;

@RestController
@RequestMapping("/banklogin")
@Scope("session")
@CrossOrigin
public class Adminlogin {

	@Autowired
	private Bank_AdminService service;
	private Integer bankerId;

	@GetMapping("/{userId}")
	public ResponseEntity<String> bankerLogin(@PathVariable("userId") String userId) {
		ResponseEntity<String> result;
		try {
			Banker banker = service.getBankerDetails(userId);
			bankerId = banker.getBankerId();
			result = new ResponseEntity<String>("logged in succesfully", HttpStatus.OK);
		} catch (IBSExceptions e) {
			result = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
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