package it.polito.ezshop.data;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class SaleTransactionTest {

	@Test
	public void testSetTicketNumber() {
		SaleTransactionImpl saleTr = new SaleTransactionImpl();

		Integer ticketNumber = 64;
		saleTr.setTicketNumber(ticketNumber);
		assertEquals(ticketNumber, saleTr.getTicketNumber());
	}

	@Test
	public void testSetEntries() {
		SaleTransactionImpl saleTr = new SaleTransactionImpl();

		List<TicketEntry> ticketList = new ArrayList<>();

		TicketEntryImpl tk1 = new TicketEntryImpl();
		TicketEntryImpl tk2 = new TicketEntryImpl();

		ticketList.add(tk1);
		ticketList.add(tk2);

		saleTr.setEntries(ticketList);
		assertEquals(ticketList, saleTr.getEntries());
	}

	@Test
	public void testSetDiscountRate() {
		SaleTransactionImpl saleTr = new SaleTransactionImpl();

		double discountRate = 12.50;
		saleTr.setDiscountRate(discountRate);
		assert (discountRate == saleTr.getDiscountRate());
	}

	@Test
	public void testSetPrice() {
		SaleTransactionImpl saleTr = new SaleTransactionImpl();

		double price = 24.99;
		saleTr.setPrice(price);
		assert (price == saleTr.getPrice());
	}

	@Test
	public void testSetPayment() {
		SaleTransactionImpl saleTr = new SaleTransactionImpl();

		BalanceOperationImpl balanceOp = new BalanceOperationImpl();
		saleTr.setPayment(balanceOp);
		assertEquals(balanceOp, saleTr.getPayment());
	}
}
