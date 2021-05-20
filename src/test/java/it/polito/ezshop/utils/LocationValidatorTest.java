package it.polito.ezshop.utils;

import static org.junit.Assert.*;

import org.junit.Test;



public class LocationValidatorTest {

	@Test
	public void testValid() {
		LocationValidator locVal = new LocationValidator();
		
		assertTrue(locVal.isValid("1-a-1", null));
		assertFalse(locVal.isValid("3131ad", null));
		assertTrue(locVal.isValid("", null));
		assertTrue(locVal.isValid(null, null));
	}

}
