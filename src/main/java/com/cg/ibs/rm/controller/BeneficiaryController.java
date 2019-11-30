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
import com.cg.ibs.rm.service.BeneficiaryAccountService;

@RestController
@RequestMapping("/beneficiary")
@Scope("session")
@CrossOrigin
public class BeneficiaryController {

	UserLogin controller = new UserLogin();

	@Autowired
	private BeneficiaryAccountService beneficiaryservice;

	@PostMapping
	public ResponseEntity<String> addSameBeneficiary(@RequestBody Beneficiary beneficiary) throws IBSExceptions {
		ResponseEntity<String> result = null;
		if (beneficiaryservice.validateBeneficiaryAccountNameOrBankName(beneficiary.getAccountName())
				&& beneficiaryservice.validateBeneficiaryAccountNumber(beneficiary.getAccountNumber().toString())) {
			beneficiary.setBankName("IBS");
			beneficiary.setIfscCode("IBS45623778");
			beneficiary.setTimestamp(LocalDateTime.now());
			beneficiaryservice.saveBeneficiaryAccountDetails(controller.getUci(), beneficiary);
			result = new ResponseEntity<String>("Beneficiary gone for approval", HttpStatus.OK);
		} else {
			result = new ResponseEntity<String>("format incorrect", HttpStatus.NOT_ACCEPTABLE);
		}
		return result;
	}

	@PostMapping("/otherbank")
	public ResponseEntity<String> addOtherBeneficiary(@RequestBody Beneficiary beneficiary) throws IBSExceptions {
		ResponseEntity<String> result = null;
		if (beneficiaryservice.validateBeneficiaryAccountNameOrBankName(beneficiary.getAccountName())
				&& beneficiaryservice.validateBeneficiaryAccountNumber(beneficiary.getAccountNumber().toString())
				&& beneficiaryservice.validateBeneficiaryIfscCode(beneficiary.getIfscCode())
				&& beneficiaryservice.validateBeneficiaryAccountNameOrBankName(beneficiary.getBankName())) {
			beneficiary.setTimestamp(LocalDateTime.now());
			beneficiaryservice.saveBeneficiaryAccountDetails(controller.getUci(), beneficiary);
			result = new ResponseEntity<String>("Beneficiary gone for approval", HttpStatus.OK);
		} else {
			result = new ResponseEntity<String>("format incorrect", HttpStatus.NOT_ACCEPTABLE);
		}
		return result;
	}

	@GetMapping
	public ResponseEntity<Set<Beneficiary>> viewBeneficiaries() throws IBSExceptions {
		ResponseEntity<Set<Beneficiary>> result = null;
		Set<Beneficiary> beneficiaries;

		beneficiaries = beneficiaryservice.showBeneficiaryAccount(controller.getUci());
		result = new ResponseEntity<Set<Beneficiary>>(beneficiaries, HttpStatus.OK);

		return result;
	}

	@DeleteMapping("/{accountNumber}")
	public ResponseEntity<String> deletebeneficiary(@PathVariable("accountNumber") BigInteger accountNumber)
			throws IBSExceptions {
		ResponseEntity<String> result = null;

		beneficiaryservice.deleteBeneficiaryAccountDetails(accountNumber);
		result = new ResponseEntity<String>("Account deleted successfully", HttpStatus.OK);

		return result;
	}

	@PutMapping("/modifyinother/{accountNumber}")
	public ResponseEntity<String> modifybeneficiary(@PathVariable("accountNumber") BigInteger accountNumber,
			@RequestBody Beneficiary beneficiary) throws IBSExceptions, IOException {
		ResponseEntity<String> result = null;
		if (beneficiaryservice.validateBeneficiaryAccountNameOrBankName(beneficiary.getAccountName())
				&& beneficiaryservice.validateBeneficiaryAccountNumber(beneficiary.getAccountNumber().toString())
				&& beneficiaryservice.validateBeneficiaryIfscCode(beneficiary.getIfscCode())
				&& beneficiaryservice.validateBeneficiaryAccountNameOrBankName(beneficiary.getBankName())) {
			beneficiary.setTimestamp(LocalDateTime.now());

			beneficiaryservice.modifyBeneficiaryAccountDetails(accountNumber, beneficiary);
			result = new ResponseEntity<String>("Account gone for modification", HttpStatus.OK);

		} else {
			result = new ResponseEntity<String>("format incorrect", HttpStatus.NOT_ACCEPTABLE);
		}
		return result;

	}

	@PutMapping("/{accountNumber}")
	public ResponseEntity<String> modifyIbsBeneficiary(@PathVariable("accountNumber") BigInteger accountNumber,
			@RequestBody Beneficiary beneficiary) throws IBSExceptions, IOException {
		ResponseEntity<String> result = null;
		if (beneficiaryservice.validateBeneficiaryAccountNameOrBankName(beneficiary.getAccountName())
				&& beneficiaryservice.validateBeneficiaryAccountNumber(beneficiary.getAccountNumber().toString())) {
			beneficiary.setTimestamp(LocalDateTime.now());
			beneficiary.setBankName("IBS");
			beneficiary.setIfscCode("IBS45623778");
			beneficiaryservice.modifyBeneficiaryAccountDetails(accountNumber, beneficiary);
			result = new ResponseEntity<String>("Account gone for modification", HttpStatus.OK);

		} else {
			result = new ResponseEntity<String>("format incorrect", HttpStatus.NOT_ACCEPTABLE);
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
