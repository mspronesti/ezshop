package it.polito.ezshop.acceptanceTest;

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
	
	public void testSetCustomerCard() {
		CustomerImpl customer = new CustomerImpl();
		
		String custCard = "14148";
		
		customer.setCustomerName(custCard);
		assertEquals(custCard, customer.getCustomerCard());
	}
	
	public void testGetId() {
		CustomerImpl customer = new CustomerImpl();
		
		Integer id = 414276;
		
		customer.setId(id);
		assertEquals(id, customer.getId());
	}
	
	public void testSetPoint() {
		CustomerImpl customer = new CustomerImpl();
		
		Integer point = 14;
		
		
		customer.setPoints(point);
		assertEquals(point, customer.getPoints());
	}
	
	

}
