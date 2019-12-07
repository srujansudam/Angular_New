package com.cg.ibs.rm.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

//import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cg.ibs.rm.dao.CreditCardDAO;
import com.cg.ibs.rm.exception.IBSExceptions;
import com.cg.ibs.rm.model.CreditCard;
import com.cg.ibs.rm.ui.CardStatus;

@Service("creditCardService")
public class CreditCardServiceImpl implements CreditCardService {
	// private static Logger logger = Logger.getLogger(CreditCardServiceImpl.class);
	@Autowired
	private CreditCardDAO creditCardDao;

	@Override
	public Set<CreditCard> showCardDetails(BigInteger uci) throws IBSExceptions {
		// logger.info("entering into showCardDetails method of CreditCardServiceImpl
		// class");
		return creditCardDao.getDetails(uci);

	}

	@Override
	public boolean validateCardNumber(String creditCardNumber) {
		// logger.info("entering into validateCardNumber method of CreditCardServiceImpl
		// class");
		boolean validNumber = false;
		if (Pattern.matches("^[0-9]{16}$", creditCardNumber)) {
			validNumber = true;
		}
		return validNumber;
	}

	@Override
	public boolean validateDateOfExpiry(String creditCardExpiry) {
		// logger.info("entering into validateDateOfExpiry method of
		// CreditCardServiceImpl class");
		LocalDate today = LocalDate.now();
		boolean validDate = false;
		if (Pattern.matches("^(([12][0-9]{3})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01]))$", creditCardExpiry)) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate creditDateOfExpiry = LocalDate.parse(creditCardExpiry, formatter);
			if (!creditDateOfExpiry.isBefore(today)) {
				validDate = true;
			} else {
				validDate = false;
			}
		}
		return validDate;
	}

	@Override
	public boolean validateNameOnCard(String nameOnCreditCard) {
		// logger.info("entering into validateNameOnCard method of CreditCardServiceImpl
		// class");
		boolean validName = false;
		if (Pattern.matches("^[a-zA-z]+([\\s][a-zA-Z]+)*$", nameOnCreditCard))
			validName = true;
		return validName;
	}

	@Override
	@Transactional
	public boolean deleteCardDetails(BigInteger creditCardNumber) throws IBSExceptions {// delete method for customer
																						// use
		// logger.info("entering into deleteCardDetails method of CreditCardServiceImpl
		// class");
		boolean result = creditCardDao.deleteDetails(creditCardNumber);
		return result;
	}

	@Override
	@Transactional
	public boolean saveCardDetails(BigInteger uci, CreditCard card) throws IBSExceptions {// save for bank_admin check
		// logger.info("entering into saveCardDetails method of CreditCardServiceImpl
		// class");
		boolean check = false;
		card.setIncome(0.0);
		card.setCardStatus(CardStatus.PENDING);
		card.setCardType(" ");
		card.setCreditLimit(new BigDecimal("0"));
		card.setCreditScore(0);
		card.setCurrentPin(" ");
		card.setCvvNum(" ");
		Random r = new Random();
		card.setBankId(r.nextInt(5) + 1);
		if (creditCardDao.copyDetails(uci, card)) {
			check = true;
		}
		return check;

	}
}
