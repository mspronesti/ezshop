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
	public void testSetCustomerCard() {
		CustomerImpl customer = new CustomerImpl();
		LoyaltyCard loyCard = new LoyaltyCardImpl();
		String custCard = "1763985854";
		loyCard.setId(custCard);

		
		customer.setCustomerCard(custCard);
		assertEquals(custCard, customer.getCustomerCard());
	}
	@Test
	public void testGetId() {
		CustomerImpl customer = new CustomerImpl();
		
		Integer id = 414276;
		
		customer.setId(id);
		assertEquals(id, customer.getId());
	}
	@Test
	public void testSetPoint() {
		CustomerImpl customer = new CustomerImpl();
		LoyaltyCard loyCard = new LoyaltyCardImpl();
		String custCard = "1763985854";

		loyCard.setId(custCard);
		customer.setCustomerCard(custCard);

		Integer point = 14;
		customer.setPoints(point);
		assertEquals(point, customer.getPoints());
	}
	
	

}
