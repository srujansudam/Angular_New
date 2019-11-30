package com.cg.ibs.rm.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.ibs.rm.model.BankerHistory;
import com.cg.ibs.rm.service.Bank_AdminService;

@RestController
@RequestMapping("/bankAdminHistory")
@Scope("session")
@CrossOrigin
public class AdminController {

	@Autowired
	private Bank_AdminService service;
	private Integer bankerId;


	@GetMapping
	public ResponseEntity<Set<BankerHistory>> showBenHistory() {
		Set<BankerHistory> history = service.getBenHistory(bankerId);
		ResponseEntity<Set<BankerHistory>> result;
		if (history.isEmpty())
			result = new ResponseEntity<Set<BankerHistory>>(history, HttpStatus.NO_CONTENT);
		else
			result = new ResponseEntity<Set<BankerHistory>>(history, HttpStatus.OK);
		return result;
	}

	@GetMapping("/cardRequests")
	public ResponseEntity<Set<BankerHistory>> showCreditHistory() {
		Set<BankerHistory> history = service.getCreditHistory(bankerId);
		ResponseEntity<Set<BankerHistory>> result;
		if (history.isEmpty())
			result = new ResponseEntity<Set<BankerHistory>>(history, HttpStatus.NO_CONTENT);
		else
			result = new ResponseEntity<Set<BankerHistory>>(history, HttpStatus.OK);
		return result;
	}



	
}