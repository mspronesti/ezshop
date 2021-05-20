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
	public void testGetId() {
		CustomerImpl customer = new CustomerImpl();
		
		Integer id = 414276;
		
		customer.setId(id);
		assertEquals(id, customer.getId());
	}
}
