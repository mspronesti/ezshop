package it.polito.ezshop.data;

import static org.junit.Assert.*;

import org.junit.Test;

public class CustomerTest {

	@Test
	public void testSetCustomerName() {
		CustomerImpl customer = new CustomerImpl();
		
		String custName = "MarcoA";
		
		customer.setCustomerName(custName);
		assertEquals(custName, customer.getCustomerName());
	}
	@Test
	public void testSetId() {
		CustomerImpl customer = new CustomerImpl();
		
		Integer id = 414276;
		
		customer.setId(id);
		assertEquals(id, customer.getId());
	}

	//Tests for integration test

	@Test
	public void testSetCustomerCard(){
		CustomerImpl customer = new CustomerImpl();
		LoyaltyCardImpl loyaltyCard = new LoyaltyCardImpl();

		String customerCard="6723006599";
		loyaltyCard.setCustomer(customer);
		loyaltyCard.setId(customerCard);
		customer.setCustomerCard(customerCard);
		customer.loyaltyCard=loyaltyCard;

		assertEquals(customerCard, customer.getCustomerCard());
	}

	@Test
	public void testSetPoints(){
		CustomerImpl customer = new CustomerImpl();
		LoyaltyCardImpl loyaltyCard = new LoyaltyCardImpl();

		String customerCard="6723006599";
		Integer points=30;
		loyaltyCard.setCustomer(customer);
		loyaltyCard.setId(customerCard);
		customer.setCustomerCard(customerCard);
		customer.loyaltyCard=loyaltyCard;
		assertEquals(customerCard, customer.getCustomerCard());
		customer.setPoints(points);

		assertEquals(customerCard, customer.getCustomerCard());
	}


	//Test for integration test of LoyaltyCard which has circular dependency with Customer class

	@Test
	public void testSetCustomerImpl() {
		LoyaltyCardImpl loyaltyC = new LoyaltyCardImpl();
		CustomerImpl customer = new CustomerImpl();

		loyaltyC.setCustomer(customer);
		assertEquals(customer, loyaltyC.getCustomer());
	}
}
