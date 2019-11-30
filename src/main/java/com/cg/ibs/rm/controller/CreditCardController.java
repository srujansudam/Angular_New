package com.cg.ibs.rm.controller;

import java.math.BigInteger;
import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.ibs.rm.exception.IBSExceptions;
import com.cg.ibs.rm.model.CreditCard;
import com.cg.ibs.rm.service.CreditCardService;

@RestController
@RequestMapping("/creditCard")
@Scope("session")
@CrossOrigin
public class CreditCardController {

	@Autowired
	private CreditCardService creditCard;
	UserLogin controller = new UserLogin();

	@PostMapping
	public ResponseEntity<String> addCard(@RequestBody CreditCard card) throws IBSExceptions {
		ResponseEntity<String> result = null;
		LocalDate date = LocalDate.of(card.getYear(), card.getMonth(), 27);

		if (creditCard.validateCardNumber(card.getCardNumber().toString())
				&& creditCard.validateDateOfExpiry(date.toString())
				&& creditCard.validateNameOnCard(card.getNameOnCard())) {
			card.setDateOfExpiry(date);
			card.setTimestamp(LocalDateTime.now());
			creditCard.saveCardDetails(controller.getUci(), card);
			result = new ResponseEntity<String>("Card gone for approval", HttpStatus.OK);
		} else {
			result = new ResponseEntity<String>("format incorrect", HttpStatus.NOT_ACCEPTABLE);
		}
		return result;
	}

	@GetMapping
	public ResponseEntity<Set<CreditCard>> viewCardDetails() throws IBSExceptions {
		ResponseEntity<Set<CreditCard>> result = null;
		Set<CreditCard> cards = creditCard.showCardDetails(controller.getUci());
		result = new ResponseEntity<Set<CreditCard>>(cards, HttpStatus.OK);
		return result;
	}

	@DeleteMapping("/{cardNumber}")
	public ResponseEntity<String> deleteCreditCard(@PathVariable("cardNumber") BigInteger cardNumber)
			throws IBSExceptions {
		ResponseEntity<String> result = null;
		creditCard.deleteCardDetails(cardNumber);
		result = new ResponseEntity<String>("Card deleted successfully", HttpStatus.OK);
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