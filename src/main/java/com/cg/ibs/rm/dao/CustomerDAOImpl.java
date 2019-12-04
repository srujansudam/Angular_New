package com.cg.ibs.rm.dao;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.cg.ibs.rm.exception.ExceptionMessages;
import com.cg.ibs.rm.exception.IBSExceptions;
import com.cg.ibs.rm.model.Customer;

@Repository("CustomerDao")
public class CustomerDAOImpl implements CustomerDAO {

	@PersistenceContext
	EntityManager manager;

	public String returnName(BigInteger uci) {
		Customer customerBean = manager.find(Customer.class, uci);
		String lastName = customerBean.getLastname();
		if (null == lastName) {
			lastName = " ";
		}
		return customerBean.getFirstName() + " " + lastName;
	}

	public BigInteger returnUCI(String userID) throws IBSExceptions {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		BigInteger uci = null;
		CriteriaQuery<BigInteger> query = builder.createQuery(BigInteger.class);
		Root<Customer> custRoot = query.from(Customer.class);
		query.select(custRoot.<BigInteger>get("uci")).where(builder.equal(custRoot.get("userId"), userID));
		try {
			uci = manager.createQuery(query).getSingleResult();
		} catch (Exception e) {
			throw new IBSExceptions(ExceptionMessages.NO_CUSTOMERS);
		}

		return uci;
	}

	public Set<BigInteger> getUciList() throws IBSExceptions {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<BigInteger> query = builder.createQuery(BigInteger.class);
		Root<Customer> custRoot = query.from(Customer.class);
		query.select(custRoot.<BigInteger>get("uci"));
		List<BigInteger> ucis = manager.createQuery(query).getResultList();
		if (null == ucis) {
			throw new IBSExceptions("No customers");
		} else {
			return new HashSet<>(ucis);
		}

	}

	public Customer getCustomer(String userId) throws IBSExceptions {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		BigInteger uci = null;
		Customer customer;
		CriteriaQuery<BigInteger> query = builder.createQuery(BigInteger.class);
		Root<Customer> custRoot = query.from(Customer.class);
		query.select(custRoot.<BigInteger>get("uci")).where(builder.equal(custRoot.get("userId"), userId));
		try {
			uci = manager.createQuery(query).getSingleResult();
			customer = manager.find(Customer.class, uci);
		} catch (Exception e) {
			throw new IBSExceptions(ExceptionMessages.NO_CUSTOMERS);
		}
		return customer;
	}

	public CustomerDAOImpl() {
		super();

	}
}
