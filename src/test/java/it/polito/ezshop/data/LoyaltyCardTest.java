package it.polito.ezshop.data;

import static org.junit.Assert.*;

import org.junit.Test;

public class LoyaltyCardTest {

	@Test
	public void testSetId() {
		LoyaltyCardImpl loyaltyC = new LoyaltyCardImpl();
		
		String id="1763985854";
		loyaltyC.setId(id);
		assertEquals(id, loyaltyC.getId());
	}
	
	@Test
	public void testSetPoints() {
		LoyaltyCardImpl loyaltyC = new LoyaltyCardImpl();
		
		Integer points=50;
		loyaltyC.setPoints(points);
		assertEquals(points, loyaltyC.getPoints());
	}

	//Tests for integration test

	@Test
	public void testSetCustomerImpl() {
		LoyaltyCardImpl loyaltyC = new LoyaltyCardImpl();
		
		CustomerImpl customer = new CustomerImpl();
		loyaltyC.setCustomer(customer);
		assertEquals(customer, loyaltyC.getCustomer());
	}
}
