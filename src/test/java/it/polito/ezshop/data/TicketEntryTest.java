package it.polito.ezshop.data;

import static org.junit.Assert.*;

import org.junit.Test;

public class TicketEntryTest {

	@Test
	public void testSetBarcode() {
		TicketEntryImpl ticket = new TicketEntryImpl();
		
		String barcode="313151";
		ticket.setBarCode(barcode);
		assertEquals(barcode, ticket.getBarCode());
	}
	
	@Test
	public void testSetProductDescription() {
		TicketEntryImpl ticket = new TicketEntryImpl();
		
		String productDesc="Yellow T-shirt";
		ticket.setProductDescription(productDesc);
		assertEquals(productDesc, ticket.getProductDescription());
	}
	
	@Test
	public void testSetAmount() {
		TicketEntryImpl ticket = new TicketEntryImpl();
		
		int amount=12;
		ticket.setAmount(amount);
		assertEquals(amount, ticket.getAmount());
	}
	
	@Test
	public void testSetPricePerUnit() {
		TicketEntryImpl ticket = new TicketEntryImpl();
		
		double price=6.24;
		ticket.setPricePerUnit(price);
		assert(price==ticket.getPricePerUnit());
	}

	@Test
	public void testSetDiscountRate() {
		TicketEntryImpl ticket = new TicketEntryImpl();
		
		double discount=33.33;
		ticket.setDiscountRate(discount);
		assert(discount==ticket.getDiscountRate());
	}
}
