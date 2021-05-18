package it.polito.ezshop.acceptanceTest;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

public class BalanceOperationTest {

	@Test
	public void testSetBalanceId() {
		BalanceOperationImpl balanceOp = new BalanceOperationImpl();
		
		int id = 313124;
		balanceOp.setBalanceId(id);
		assert(id==balanceOp.getBalanceId());
	}

	@Test
	public void testSetDate() {
		BalanceOperationImpl balanceOp = new BalanceOperationImpl();
		
		LocalDate date=LocalDate.of(2019, 3, 13);
		balanceOp.setDate(date);
		assertEquals(date, balanceOp.getDate());
	}
	
	@Test
	public void testSetMoney() {
		BalanceOperationImpl balanceOp = new BalanceOperationImpl();
		
		double money = 31.90;
		balanceOp.setMoney(money);
		assert(money==balanceOp.getMoney());
	}
	
	@Test
	public void testSetType() {
		BalanceOperationImpl balanceOp = new BalanceOperationImpl();
		
		String type = "DEBIT";
		balanceOp.setType(type);
		assertEquals(type, balanceOp.getType());
		
		type = "CREDIT";
		balanceOp.setType(type);
		assertEquals(type, balanceOp.getType());
	}
}
