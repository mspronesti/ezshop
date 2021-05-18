package it.polito.ezshop.acceptanceTest;

import static org.junit.Assert.*;

import org.junit.Test;

public class UserTest {

	@Test
	public void testSetId() {
		UserImpl user= new UserImpl();
		
		Integer id=2312;
		user.setId(id);
		assertEquals(id, user.getId());
	}
	
	@Test
	public void testSetUsername() {
		UserImpl user= new UserImpl();
		
		String username="FilippoA";
		user.setUsername(username);
		assertEquals(username, user.getUsername());
	}
	
	@Test
	public void testSetPassword() {
		UserImpl user= new UserImpl();
		
		String password="Fe29dqh^ad3_ad";
		user.setPassword(password);
		assertEquals(password, user.getPassword());
	}
	
	@Test
	public void testSetRole() {
		UserImpl user= new UserImpl();
		
		String role="Cashier";
		user.setRole(role);
		assertEquals(role, user.getRole());
	}

}
