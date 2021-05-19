package it.polito.ezshop.data;

import static org.junit.Assert.*;

import org.junit.Test;

public class OrderTest {

	@Test
	public void testSetBalanceId() {
		OrderImpl order = new OrderImpl();
		
		Integer balanceId = 43123;
		order.setBalanceId(balanceId);
		assertEquals(balanceId, order.getBalanceId());
	}
	
	@Test
	public void testSetProductCode() {
		OrderImpl order = new OrderImpl();
		
		String productCode = "01DA2";
		order.setProductCode(productCode);
		assertEquals(productCode, order.getProductCode());
	}
	
	@Test
	public void testSetPricePerUnit() {
		OrderImpl order = new OrderImpl();
		
		double price = 12.50;
		order.setPricePerUnit(price);
		assert(price==order.getPricePerUnit());
	}
	
	@Test
	public void testSetQuantity() {
		OrderImpl order = new OrderImpl();
		
		int quantity = 6;
		order.setQuantity(quantity);
		assertEquals(quantity, order.getQuantity());
	}
	
	@Test
	public void testSetStatus() {
		OrderImpl order = new OrderImpl();
		
		String status = "Pending";
		order.setStatus(status);
		assertEquals(status, order.getStatus());
	}
	
	@Test
	public void testSetOrderId() {
		OrderImpl order = new OrderImpl();
		
		Integer orderId = 16537;
		order.setOrderId(orderId);
		assertEquals(orderId, order.getOrderId());
	}

}
