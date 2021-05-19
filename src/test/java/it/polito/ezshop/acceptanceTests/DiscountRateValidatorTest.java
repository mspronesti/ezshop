package it.polito.ezshop.acceptanceTests;

import static org.junit.Assert.*;

import java.io.InvalidObjectException;

import org.junit.Test;

import it.polito.ezshop.util.DiscountRateValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DiscountRateValidatorTest {

	@Test
	public void testIsValid() {
		DiscountRateValidator dRateValid= new DiscountRateValidator();
		
		assertFalse(dRateValid.isValid(24d, null));
		assertTrue(dRateValid.isValid(0.5d, null));
		assertThrows(NullPointerException.class, () -> {dRateValid.isValid(null, null);});
	}

}
