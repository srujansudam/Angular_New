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
import com.cg.ibs.rm.model.Message;
import com.cg.ibs.rm.service.CreditCardService;

@RestController
@RequestMapping("/creditCard/{uci}")
@Scope("session")
@CrossOrigin
public class CreditCardController {

	@Autowired
	private CreditCardService creditCard;

	@PostMapping
	public ResponseEntity<Message> addCard(@RequestBody CreditCard card, @PathVariable("uci") String uci) throws IBSExceptions {
		ResponseEntity<Message> result = null;
		LocalDate date= card.getDateOfExpiry();
		String dateStr = date.toString().substring(0, 10);
		if (creditCard.validateCardNumber(card.getCardNumber().toString())
				&& creditCard.validateDateOfExpiry(dateStr))
//				&& creditCard.validateNameOnCard(card.getNameOnCard())) 
		{
			card.setTimestamp(LocalDateTime.now());
			creditCard.saveCardDetails(new BigInteger(uci), card);
			result = new ResponseEntity<>(new Message("Card gone for Approval", null, null), HttpStatus.OK);
		} else {
			result = new ResponseEntity<>(new Message("format incorrect", null, null), HttpStatus.NOT_ACCEPTABLE);
		}
		return result;
	}

	@GetMapping
	public ResponseEntity<Set<CreditCard>> viewCardDetails(@PathVariable("uci") String uci) throws IBSExceptions {
		ResponseEntity<Set<CreditCard>> result = null;
		Set<CreditCard> cards = creditCard.showCardDetails(new BigInteger(uci));
		result = new ResponseEntity<Set<CreditCard>>(cards, HttpStatus.OK);
		return result;
	}

	@DeleteMapping("/{cardNumber}")
	public ResponseEntity<String> deleteCreditCard(@PathVariable("cardNumber") BigInteger cardNumber)
			throws IBSExceptions {
		String exc = null;
		ResponseEntity<String> result = null;
		creditCard.deleteCardDetails(cardNumber);
		exc = "Card deleted successfully";
		result = new ResponseEntity<String>(exc, HttpStatus.OK);
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