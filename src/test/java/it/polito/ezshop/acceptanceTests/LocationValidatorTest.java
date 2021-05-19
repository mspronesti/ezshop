package it.polito.ezshop.acceptanceTests;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polito.ezshop.util.LocationValidator;

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
