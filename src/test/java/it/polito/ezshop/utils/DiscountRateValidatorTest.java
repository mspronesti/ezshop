package it.polito.ezshop.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class DiscountRateValidatorTest {

	@Test
	public void testIsValid() {
		DiscountRateValidator dRateValid= new DiscountRateValidator();
		
		assertFalse(dRateValid.isValid(24d, null));
		assertTrue(dRateValid.isValid(0.5d, null));
		assertThrows(NullPointerException.class, () -> dRateValid.isValid(null, null));
	}

}
