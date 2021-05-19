package it.polito.ezshop.data;

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
	@Test
	public void testSetLocation() {
		ProductTypeImpl productT = new ProductTypeImpl();
		
		String location = "3-a-12";
		productT.setLocation(location);
		assertEquals(location, productT.getLocation());
	}
	@Test
	public void testSetNote() {
		ProductTypeImpl productT = new ProductTypeImpl();
		
		String note = "Blue version";
		productT.setNote(note);
		assertEquals(note, productT.getNote());
	}
	@Test
	public void testSetProductDescription() {
		ProductTypeImpl productT = new ProductTypeImpl();
		
		String description = "Blue t-shirt";
		productT.setProductDescription(description);
		assertEquals(description, productT.getProductDescription());
	}
	@Test
	public void testSetBarCode() {
		ProductTypeImpl productT = new ProductTypeImpl();
		
		String barcode = "13148419";
		productT.setBarCode(barcode);
		assertEquals(barcode, productT.getBarCode());
	}
	@Test
	public void testSetPricePerUnit() {
		ProductTypeImpl productT = new ProductTypeImpl();
		
		double price = 15.24;
		productT.setPricePerUnit(price);
		assert(price==productT.getPricePerUnit());
	}
	@Test
	public void testSetId() {
		ProductTypeImpl productT = new ProductTypeImpl();

		Integer id = 3214;
		productT.setId(id);
		assertEquals(id, productT.getId());
	}

}
