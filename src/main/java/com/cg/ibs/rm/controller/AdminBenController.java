package com.cg.ibs.rm.controller;

import java.math.BigInteger;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.ibs.rm.exception.IBSExceptions;
import com.cg.ibs.rm.model.Beneficiary;
import com.cg.ibs.rm.model.Message;
import com.cg.ibs.rm.service.Bank_AdminService;

@RestController
@RequestMapping("/bankAdminBen")
@Scope("session")
@CrossOrigin
public class AdminBenController {

	@Autowired
	private Bank_AdminService service;
	private Integer bankerId;

	@GetMapping
	public ResponseEntity<Set<Beneficiary>> showUnapprovedBenRequests() {
		Set<Beneficiary> benList = service.showAllUnapprovedBeneficiaries();
		ResponseEntity<Set<Beneficiary>> result;
		if (benList.isEmpty())
			result = new ResponseEntity<Set<Beneficiary>>(benList, HttpStatus.NO_CONTENT);
		else
			result = new ResponseEntity<Set<Beneficiary>>(benList, HttpStatus.OK);
		return result;
	}

	@GetMapping("/{bankerId1}")
	public ResponseEntity<Set<Beneficiary>> showUnapprovedBenRequestsForMe(@PathVariable("bankerId1") Integer bankerId) {
		Set<Beneficiary> benList = service.showUnapprovedBeneficiariesForMe(bankerId);
		ResponseEntity<Set<Beneficiary>> result;
		if (benList.isEmpty())
			result = new ResponseEntity<Set<Beneficiary>>(benList, HttpStatus.NO_CONTENT);
		else
			result = new ResponseEntity<Set<Beneficiary>>(benList, HttpStatus.OK);
		return result;
	}

	@GetMapping("/{accountNumber}/{decision}")
	public ResponseEntity<Message> acceptBeneficiaries(@PathVariable("accountNumber") BigInteger accountNumber,
			@PathVariable("decision") String decision) throws IBSExceptions {
		ResponseEntity<Message> result;
		if (decision.equalsIgnoreCase("approve")) {
			service.saveBeneficiaryDetails(accountNumber);
			result = new ResponseEntity<>(new Message("Approved", null, null), HttpStatus.OK);
		} else  {
			service.disapproveBenficiary(accountNumber);
			result = new ResponseEntity<>(new Message("Disapproved", null, null), HttpStatus.OK);

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