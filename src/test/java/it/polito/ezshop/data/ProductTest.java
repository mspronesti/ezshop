package it.polito.ezshop.data;

import org.junit.Test;

import static org.junit.Assert.*;

public class ProductTest {

    @Test
    public void testSetId() {
        String productId = "10000";
        ProductImpl product =  new ProductImpl();
        product.setId(productId);

        assertEquals(productId, product.getId());
    }

    @Test
    public void testSetProductType() {
        ProductImpl product = new ProductImpl();
        ProductTypeImpl productType = new ProductTypeImpl();
        productType.setBarCode("012345678905");


        product.setProductType(productType);
        assertEquals(productType.getBarCode(),product.getProductType().getBarCode());
    }
}