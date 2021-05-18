package it.polito.ezshop.acceptanceTest;

import static org.junit.Assert.*;

import org.junit.Test;

public class LoyaltyCardTest {

	@Test
	public void testSetId() {
		LoyaltyCardImpl loyaltyC = new LoyaltyCardImpl();
		
		Integer id=14213;
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
	
	@Test
	public void testSetCustomerImpl() {
		LoyaltyCardImpl loyaltyC = new LoyaltyCardImpl();
		
		CustomerImpl customer = new CustomerImpl();
		loyaltyC.setCustomer(customer);;
		assertEquals(customer, loyaltyC.getCustomer());
	}
}
