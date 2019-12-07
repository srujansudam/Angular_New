package com.cg.ibs.rm.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.ibs.rm.exception.IBSExceptions;
import com.cg.ibs.rm.model.Beneficiary;
import com.cg.ibs.rm.model.Message;
import com.cg.ibs.rm.service.BeneficiaryAccountService;

@RestController
@RequestMapping("/beneficiary/{uci}")
@Scope("session")
@CrossOrigin
public class BeneficiaryController {

	UserLogin controller = new UserLogin();

	@Autowired
	private BeneficiaryAccountService beneficiaryservice;

	@PostMapping
	public ResponseEntity<Message> addSameBeneficiary(@RequestBody Beneficiary beneficiary,
			@PathVariable("uci") String uci) throws IBSExceptions {
		ResponseEntity<Message> result = null;
		if (beneficiaryservice.validateBeneficiaryAccountNameOrBankName(beneficiary.getAccountName())
				&& beneficiaryservice.validateBeneficiaryAccountNumber(beneficiary.getAccountNumber().toString())) {
			beneficiary.setBankName("IBS");
			beneficiary.setIfscCode("IBS45623778");
			beneficiary.setTimestamp(LocalDateTime.now());
			beneficiaryservice.saveBeneficiaryAccountDetails(new BigInteger(uci), beneficiary);
			result = new ResponseEntity<>(new Message("Beneficiary gone for approval", null, null), HttpStatus.OK);
		} else {
			result = new ResponseEntity<>(new Message("format incorrect", null, null), HttpStatus.NOT_ACCEPTABLE);
		}
		return result;
	}

	@PostMapping("/otherbank")
	public ResponseEntity<Message> addOtherBeneficiary(@RequestBody Beneficiary beneficiary, @PathVariable("uci") String uci) throws IBSExceptions {
		ResponseEntity<Message> result = null;
		if (beneficiaryservice.validateBeneficiaryAccountNameOrBankName(beneficiary.getAccountName())
				&& beneficiaryservice.validateBeneficiaryAccountNumber(beneficiary.getAccountNumber().toString())
				&& beneficiaryservice.validateBeneficiaryIfscCode(beneficiary.getIfscCode())
				&& beneficiaryservice.validateBeneficiaryAccountNameOrBankName(beneficiary.getBankName())) {
			beneficiary.setTimestamp(LocalDateTime.now());
			beneficiaryservice.saveBeneficiaryAccountDetails(new BigInteger(uci), beneficiary);
			result = new ResponseEntity<>(new Message("Beneficiary gone for approval", null, null), HttpStatus.OK);
		} else {
			result = new ResponseEntity<>(new Message("format incorrect", null, null), HttpStatus.NOT_ACCEPTABLE);
		}
		return result;
	}

	@GetMapping
	public ResponseEntity<Set<Beneficiary>> viewBeneficiaries(@PathVariable("uci") String uci) throws IBSExceptions {
		ResponseEntity<Set<Beneficiary>> result = null;
		Set<Beneficiary> beneficiaries;
		beneficiaries = beneficiaryservice.showBeneficiaryAccount(new BigInteger(uci));
		result = new ResponseEntity<Set<Beneficiary>>(beneficiaries, HttpStatus.OK);
		return result;
	}

	@DeleteMapping("/{accountNumber}")
	public ResponseEntity<Message> deletebeneficiary(@PathVariable("accountNumber") BigInteger accountNumber)
			throws IBSExceptions {
		ResponseEntity<Message> result = null;

		beneficiaryservice.deleteBeneficiaryAccountDetails(accountNumber);
		result = new ResponseEntity<>(new Message("Account deleted successfully", null, null), HttpStatus.OK);

		return result;
	}

	@PutMapping("/modifyinother/{accountNumber}")	
	public ResponseEntity<Message> modifybeneficiary(@PathVariable("accountNumber") BigInteger accountNumber,
			@RequestBody Beneficiary beneficiary) throws IBSExceptions, IOException {
		ResponseEntity<Message> result = null;
		if (beneficiaryservice.validateBeneficiaryAccountNameOrBankName(beneficiary.getAccountName())
				&& beneficiaryservice.validateBeneficiaryAccountNumber(beneficiary.getAccountNumber().toString())
				&& beneficiaryservice.validateBeneficiaryIfscCode(beneficiary.getIfscCode())
				&& beneficiaryservice.validateBeneficiaryAccountNameOrBankName(beneficiary.getBankName())) {
			beneficiary.setTimestamp(LocalDateTime.now());

			beneficiaryservice.modifyBeneficiaryAccountDetails(accountNumber, beneficiary);
			result = new ResponseEntity<>(new Message("Account gone for modification", null, null), HttpStatus.OK);

		} else {
			result = new ResponseEntity<>(new Message("format incorrect", null, null), HttpStatus.NOT_ACCEPTABLE);
		}
		return result;

	}

	@PutMapping("/{accountNumber}")
	public ResponseEntity<Message> modifyIbsBeneficiary(@PathVariable("accountNumber") BigInteger accountNumber,
			@RequestBody Beneficiary beneficiary) throws IBSExceptions, IOException {
		ResponseEntity<Message> result = null;
		if (beneficiaryservice.validateBeneficiaryAccountNameOrBankName(beneficiary.getAccountName())
				&& beneficiaryservice.validateBeneficiaryAccountNumber(beneficiary.getAccountNumber().toString())) {
			beneficiary.setTimestamp(LocalDateTime.now());
			beneficiary.setBankName("IBS");
			beneficiary.setIfscCode("IBS45623778");
			beneficiaryservice.modifyBeneficiaryAccountDetails(accountNumber, beneficiary);
			result = new ResponseEntity<>(new Message("Account gone for modification", null, null), HttpStatus.OK);

		} else {
			result = new ResponseEntity<>(new Message("format incorrect", null, null), HttpStatus.NOT_ACCEPTABLE);
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
