package com.cg.ibs.rm.service;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

//import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cg.ibs.rm.dao.BeneficiaryDAO;
import com.cg.ibs.rm.exception.IBSExceptions;
import com.cg.ibs.rm.model.Beneficiary;
import com.cg.ibs.rm.ui.CardStatus;

@Service("beneficiaryAccountService")
public class BeneficiaryAccountServiceImpl implements BeneficiaryAccountService {
	// private static Logger logger =
	// Logger.getLogger(BeneficiaryAccountServiceImpl.class);
	@Autowired
	private BeneficiaryDAO beneficiaryDao;

	@Override
	public Set<Beneficiary> showBeneficiaryAccount(BigInteger uci) throws IBSExceptions {
		// logger.info("entering into showBeneficiaryAccount method of
		// BeneficiaryAccountServiceImpl class");
		return new HashSet<>(beneficiaryDao.getDetails(uci));

	}

	@Override
	public boolean validateBeneficiaryAccountNumber(String accountNumber) {
		// logger.info("entering into validateBeneficiaryAccountNumber method of
		// BeneficiaryAccountServiceImpl class");
		boolean validNumber = false;
		if (Pattern.matches("^[0-9]{11}$", accountNumber)) {
			validNumber = true;
		}
		return validNumber;
	}

	@Override
	public boolean validateBeneficiaryAccountNameOrBankName(String name) {
		// logger.info(
		// "entering into validateBeneficiaryAccountNameOrBankName method of
		// BeneficiaryAccountServiceImpl class");
		boolean validName = false;
		if (Pattern.matches("^[a-zA-z]+([\\s][a-zA-Z]+)*$", name))
			validName = true;
		return validName;
	}

	@Override
	public boolean validateBeneficiaryIfscCode(String ifsc) {
		// logger.info("entering into validateBeneficiaryIfscCode method of
		// BeneficiaryAccountServiceImpl class");
		boolean validIfsc = false;
		if (ifsc.length() == 11)
			validIfsc = true;
		return validIfsc;
	}

	@Override
	@Transactional
	public boolean modifyBeneficiaryAccountDetails(BigInteger accountNumber, Beneficiary beneficiary1)
			throws IBSExceptions {
		Beneficiary beneficiary;
		boolean validModify = false;
		// logger.info("entering into modifyBeneficiaryAccountDetails method of
		// BeneficiaryAccountServiceImpl class");
		beneficiary = beneficiaryDao.getBeneficiary(accountNumber);
		// logger.debug(beneficiary1);
		if (beneficiary1.getAccountName() != null) {
			beneficiary.setAccountName(beneficiary1.getAccountName());
		}
		if (beneficiary1.getIfscCode() != null) {
			beneficiary.setIfscCode(beneficiary1.getIfscCode());
		}
		if (beneficiary1.getBankName() != null) {
			beneficiary.setBankName(beneficiary1.getBankName());
		}
		if (beneficiaryDao.updateDetails(beneficiary)) {
			validModify = true;
		}
		// logger.debug(beneficiary1);
		return validModify;
	}

	@Override
	@Transactional
	public boolean deleteBeneficiaryAccountDetails(BigInteger accountNumber) throws IBSExceptions {
		// logger.info("entering into deleteBeneficiaryAccountDetails method of
		// BeneficiaryAccountServiceImpl class");
		boolean result = beneficiaryDao.deleteDetails(accountNumber);
		return result;
	}

	@Override
	@Transactional
	public boolean saveBeneficiaryAccountDetails(BigInteger uci, Beneficiary beneficiary) throws IBSExceptions {
		boolean check = false;
		// logger.info("entering into saveBeneficiaryAccountDetails method of
		// BeneficiaryAccountServiceImpl class");
		beneficiary.setStatus(CardStatus.PENDING);
		Random r = new Random();
		beneficiary.setBankId(r.nextInt(5) + 1);
		if (beneficiaryDao.copyDetails(uci, beneficiary)) {
			check = true;
		}
		return check;
	}

	public Beneficiary getBeneficiary(BigInteger accountNumber) throws IBSExceptions {
		Beneficiary beneficiary = beneficiaryDao.getBeneficiary(accountNumber);
		return beneficiary;
	}

}