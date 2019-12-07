package com.cg.ibs.rm.controller;

import java.math.BigInteger;
import java.util.Set;

import org.aspectj.bridge.MessageHandler;
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
import com.cg.ibs.rm.model.Account_ServiceProviders;
import com.cg.ibs.rm.model.AutoPayment;
import com.cg.ibs.rm.model.Message;
import com.cg.ibs.rm.model.ServiceProvider;
import com.cg.ibs.rm.model.ServiceProviderId;
import com.cg.ibs.rm.service.AccountService;
import com.cg.ibs.rm.service.AutoPaymentService;

@RestController
@RequestMapping("/autoPayment/{uci}")
@Scope("session")
@CrossOrigin
public class AutoPaymentComtroller {
	@Autowired
	private AutoPaymentService autoPaymentService;

	UserLogin controller = new UserLogin();

	@Autowired
	private AccountService accountService;

	@GetMapping("/accountServiceProviders")
	public ResponseEntity<Account_ServiceProviders> showAccounts(@PathVariable("uci") String uci) throws IBSExceptions {

		Account_ServiceProviders serviceProviders = new Account_ServiceProviders();
		ResponseEntity<Account_ServiceProviders> result = null;

		serviceProviders.setAccounts(accountService.getAccountsOfUci(new BigInteger(uci)));
		serviceProviders.setServiceProviders(autoPaymentService.showIBSServiceProviders());
		result = new ResponseEntity<Account_ServiceProviders>(serviceProviders, HttpStatus.OK);
		return result;
	}

	@PostMapping("/{accountNumber}")
	public ResponseEntity<Message> addAutopayment(@PathVariable("uci") String uci, @PathVariable("accountNumber") String accountNumber,
			@RequestBody AutoPayment autoPayment) throws IBSExceptions {
		ResponseEntity<Message> result = null;
		BigInteger spId = null;
		int count = 1;
		Set<ServiceProvider> serviceProviders = autoPaymentService.showIBSServiceProviders();
		for (ServiceProvider serviceProvider : serviceProviders) {

			if (serviceProvider.getNameOfCompany().equalsIgnoreCase(autoPayment.getServiceName())) {
				count++;
				spId = serviceProvider.getSpi();
			}
		}
		if (autoPaymentService.validEndDate(autoPayment.getDateOfEnd(), autoPayment.getDateOfStart()) && count != 1) {

			autoPayment.setServiceProviderId(new ServiceProviderId(spId, new BigInteger(uci)));

			autoPaymentService.autoDeduction(new BigInteger(uci), new BigInteger(accountNumber), autoPayment);
			result = new ResponseEntity<>(new Message("Autopayment added successfully", null, null), HttpStatus.OK);

		} else {
			result = new ResponseEntity<>(new Message("format incorrect", null, null), HttpStatus.NOT_ACCEPTABLE);
		}
		return result;

	}

	@GetMapping
	public ResponseEntity<Set<AutoPayment>> viewAutoPayments(@PathVariable("uci") String uci) throws IBSExceptions {

		Set<AutoPayment> autoPayments = null;
		ResponseEntity<Set<AutoPayment>> result;

		autoPayments = autoPaymentService.showAutopaymentDetails(new BigInteger(uci));
		result = new ResponseEntity<Set<AutoPayment>>(autoPayments, HttpStatus.OK);
		return result;
	}

	@PutMapping
	public ResponseEntity<Message> modifyautopaymentDetails(@RequestBody AutoPayment autoPayment, @PathVariable("uci") String uci) throws IBSExceptions {
		ResponseEntity<Message> result;

		if (autoPaymentService.validEndDate(autoPayment.getDateOfEnd(), autoPayment.getDateOfStart())) {
			if (autoPaymentService.updateDetails(new ServiceProviderId(autoPayment.getServiceProviderId().getSpi(), new BigInteger(uci)), autoPayment)) {
				result = new ResponseEntity<>(new Message("Modified autopayment", null, null), HttpStatus.OK);
			}

			else {
				result = new ResponseEntity<>(new Message("Not Modified", null, null), HttpStatus.BAD_REQUEST);
			}

		} else {
			result = new ResponseEntity<>(new Message("format incorrect",null,null), HttpStatus.NOT_ACCEPTABLE);
		}
		return result;
	}

	@PostMapping
	public ResponseEntity<Message> deleteAutoPayment(@RequestBody AutoPayment autoPayment, @PathVariable("uci") String uci) throws IBSExceptions {
		ResponseEntity<Message> result;
		if (autoPaymentService.deleteAutopayment(new BigInteger(uci), autoPayment.getServiceProviderId().getSpi())) {
			result = new ResponseEntity<>(new Message("deleted Successfully", null, null), HttpStatus.OK);
		} else {
			result = new ResponseEntity<>(new Message("Not Deleted", null, null), HttpStatus.BAD_REQUEST);
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
