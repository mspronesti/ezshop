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

		customer.setCustomerCard(customerCard);
		assertEquals(customerCard, customer.getCustomerCard());
	}

	@Test
	public void testSetPoints(){
		CustomerImpl customer = new CustomerImpl();
		LoyaltyCardImpl loyaltyCard = new LoyaltyCardImpl();
		String customerCard="6723006599";
		Integer points=141;

		customer.setCustomerCard(customerCard);

		customer.setPoints(points);
		assertEquals(customerCard, customer.getCustomerCard());
	}
}
