package it.polito.ezshop.acceptanceTest;

import static org.junit.Assert.*;

import org.junit.Test;

public class ReturnTransactionTest {

	@Test
	public void testSetSaleTransaction() {
		ReturnTransactionImpl returnT = new ReturnTransactionImpl();
		
		SaleTransactionImpl saleT = new SaleTransactionImpl();
		returnT.setSaleTransaction(saleT);
		assertEquals(saleT, returnT.getSaleTransaction());
	}

}
