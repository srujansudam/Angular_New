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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.ibs.rm.exception.IBSExceptions;
import com.cg.ibs.rm.model.CreditCard;
import com.cg.ibs.rm.model.Message;
import com.cg.ibs.rm.service.Bank_AdminService;

@RestController
@RequestMapping("/bankAdminCard")
@Scope("session")
@CrossOrigin
public class AdminCreditController {

	@Autowired
	private Bank_AdminService service;

	@GetMapping()
	public ResponseEntity<Set<CreditCard>> showUnapprovedCardRequests() {
		Set<CreditCard> cardList = service.showAllUnapprovedCreditCards();
		ResponseEntity<Set<CreditCard>> result;
		if (cardList.isEmpty()) {
			result = new ResponseEntity<Set<CreditCard>>(cardList, HttpStatus.NO_CONTENT);
		} else {
			result = new ResponseEntity<Set<CreditCard>>(cardList, HttpStatus.OK);
		}
		return result;
	}

	@GetMapping("/{bankerId2}")
	public ResponseEntity<Set<CreditCard>> showUnapprovedCardRequestsForMe(
			@PathVariable("bankerId2") Integer bankerId) {
		Set<CreditCard> cardList = service.showUnapprovedCreditCardsForMe(bankerId);
		ResponseEntity<Set<CreditCard>> result;
		if (cardList.isEmpty())
			result = new ResponseEntity<Set<CreditCard>>(cardList, HttpStatus.NO_CONTENT);
		else
			result = new ResponseEntity<Set<CreditCard>>(cardList, HttpStatus.OK);
		return result;
	}

	@GetMapping("/{cardNumber}/{decision}")
	public ResponseEntity<Message> acceptCards(@PathVariable("cardNumber") BigInteger cardNumber,
			@PathVariable("decision") String decision) throws IBSExceptions {
		ResponseEntity<Message> result;

		if (decision.equalsIgnoreCase("approve")) {
			service.saveCreditCardDetails(cardNumber);
			result = new ResponseEntity<>(new Message("Approved", null, null), HttpStatus.OK);
		} else {
			service.disapproveCreditCard(cardNumber);
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