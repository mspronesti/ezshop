package it.polito.ezshop.acceptanceTest;

import static org.junit.Assert.*;

import org.junit.Test;

public class ProductTypeTest {

	@Test
	public void testSetQuantity() {
		ProductTypeImpl productT = new ProductTypeImpl();
		
		Integer quantity=4;
		productT.setQuantity(quantity);
		assertEquals(quantity, productT.getQuantity());
	}
	
	public void testSetLocation() {
		ProductTypeImpl productT = new ProductTypeImpl();
		
		String location = "3-a-12";
		productT.setLocation(location);
		assertEquals(location, productT.getLocation());
	}
	
	public void testSetNote() {
		ProductTypeImpl productT = new ProductTypeImpl();
		
		String note = "Blue version";
		productT.setNote(note);
		assertEquals(note, productT.getNote());
	}
	
	public void testSetProductDescription() {
		ProductTypeImpl productT = new ProductTypeImpl();
		
		String description = "Blue t-shirt";
		productT.setProductDescription(description);
		assertEquals(description, productT.getProductDescription());
	}
	
	public void testSetBarCode() {
		ProductTypeImpl productT = new ProductTypeImpl();
		
		String barcode = "13148419";
		productT.setBarCode(barcode);
		assertEquals(barcode, productT.getBarCode());
	}
	
	public void testSetPricePerUnit() {
		ProductTypeImpl productT = new ProductTypeImpl();
		
		double price = 15.24;
		productT.setPricePerUnit(price);
		assert(price==productT.getPricePerUnit())
	}
	
	public void testSetId() {
		ProductTypeImpl productT = new ProductTypeImpl();
		
		Integer id=3214;
		productT.setId(id);
		assertEquals(id, productT.getId());
	}
	}

}
