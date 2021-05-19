package it.polito.ezshop.acceptanceTests;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polito.ezshop.util.GtinBarcodeValidator;

public class GtinBarcodeValidatorTest {

	@Test
	public void testIsValid() {
		GtinBarcodeValidator gtinBarcode = new GtinBarcodeValidator();
		
		assertTrue(gtinBarcode.isValid("012345678905", null));
		assertFalse(gtinBarcode.isValid("333333333333", null));
		assertFalse(gtinBarcode.isValid("ciao", null));
		assertFalse(gtinBarcode.isValid("12", null));
		assertThrows(NullPointerException.class, () -> {gtinBarcode.isValid(null, null);});
	
	}

}
