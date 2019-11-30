package com.cg.ibs.rm.controller;

import java.math.BigInteger;
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
import com.cg.ibs.rm.model.Account_ServiceProviders;
import com.cg.ibs.rm.model.AutoPayment;
import com.cg.ibs.rm.model.ServiceProvider;
import com.cg.ibs.rm.model.ServiceProviderId;
import com.cg.ibs.rm.service.AccountService;
import com.cg.ibs.rm.service.AutoPaymentService;

@RestController
@RequestMapping("/autoPayment")
@Scope("session")
@CrossOrigin
public class AutoPaymentComtroller {
	@Autowired
	private AutoPaymentService autoPaymentService;

	UserLogin controller = new UserLogin();

	@Autowired
	private AccountService accountService;

	@GetMapping("/accountServiceProviders")
	public ResponseEntity<Account_ServiceProviders> showAccounts() throws IBSExceptions {

		Account_ServiceProviders serviceProviders = new Account_ServiceProviders();
		ResponseEntity<Account_ServiceProviders> result = null;

		serviceProviders.setAccounts(accountService.getAccountsOfUci(controller.getUci()));
		serviceProviders.setServiceProviders(autoPaymentService.showIBSServiceProviders());
		result = new ResponseEntity<Account_ServiceProviders>(serviceProviders, HttpStatus.OK);
		return result;
	}

	@PostMapping("/{accountNumber}")
	public ResponseEntity<String> addAutopayment(@PathVariable("accountNumber") BigInteger accountNumber,
			@RequestBody AutoPayment autoPayment) throws IBSExceptions {
		ResponseEntity<String> result = null;
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

			autoPayment.setServiceProviderId(new ServiceProviderId(spId, controller.getUci()));

			autoPaymentService.autoDeduction(controller.getUci(), accountNumber, autoPayment);
			result = new ResponseEntity<String>("Autopayment added successfully", HttpStatus.OK);

		} else {
			result = new ResponseEntity<String>("format incorrect", HttpStatus.NOT_ACCEPTABLE);
		}
		return result;

	}

	@GetMapping
	public ResponseEntity<Set<AutoPayment>> viewAutoPayments() throws IBSExceptions {

		Set<AutoPayment> autoPayments = null;
		ResponseEntity<Set<AutoPayment>> result;

		autoPayments = autoPaymentService.showAutopaymentDetails(controller.getUci());
		result = new ResponseEntity<Set<AutoPayment>>(autoPayments, HttpStatus.OK);
		return result;
	}

	@PutMapping("/{spi}")
	public ResponseEntity<String> modifyautopaymentDetails(@RequestBody AutoPayment autoPayment,
			@PathVariable("spi") BigInteger spi) throws IBSExceptions {
		ResponseEntity<String> result;

		if (autoPaymentService.validEndDate(autoPayment.getDateOfEnd(), autoPayment.getDateOfStart())) {
			if ((autoPayment == null) || (spi == null)) {
				result = new ResponseEntity<String>("Values not entered", HttpStatus.NO_CONTENT);
			}

			else if (autoPaymentService.updateDetails(new ServiceProviderId(spi, controller.getUci()), autoPayment)) {
				result = new ResponseEntity<String>("Modified autopayment", HttpStatus.OK);
			}

			else {
				result = new ResponseEntity<String>("Not Modified", HttpStatus.BAD_REQUEST);
			}

		} else {
			result = new ResponseEntity<String>("format incorrect", HttpStatus.NOT_ACCEPTABLE);
		}
		return result;
	}

	@DeleteMapping("/{spi}")
	public ResponseEntity<String> deleteAutoPayment(@PathVariable("spi") BigInteger spId1) throws IBSExceptions {
		ResponseEntity<String> result;
		if (spId1 == null) {
			result = new ResponseEntity<String>("No details entered", HttpStatus.NO_CONTENT);
		} else if (autoPaymentService.deleteAutopayment(controller.getUci(), spId1)) {
			result = new ResponseEntity<String>("Deleted", HttpStatus.OK);
		} else {
			result = new ResponseEntity<String>("Not Deleted", HttpStatus.BAD_REQUEST);
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
