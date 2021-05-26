package it.polito.ezshop.data;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ProductTypeRepositoryTest {
    private static final ProductTypeRepository repo = new ProductTypeRepository();

    @Test
    public void find() {
        Integer productTypeId=37;
        assertEquals(productTypeId,repo.find(productTypeId).getId());
    }

    @Test
    public void findByBarcode() {
        String barcode="012345678950";
        assertEquals(barcode,repo.findByBarcode(barcode).getBarCode());
    }

    @Test
    public void findAll() {
        assertEquals(repo.findAll().getClass(), ArrayList.class);
        assertEquals(repo.findAll().get(0).getClass(), ProductTypeImpl.class);
    }

    @Test
    public void update() {
        String newNote="Recycled";
        String newLocation="12-b-2";
        double newPrice=12.99;
        Integer newQuantity=15;

        ProductType productType = repo.find(37);

        productType.setNote(newNote);
        productType.setLocation(newLocation);
        productType.setPricePerUnit(newPrice);
        productType.setQuantity(newQuantity);

        ProductType updated=repo.update(productType);

        assert(newPrice==updated.getPricePerUnit());
        assertEquals(newLocation,updated.getLocation());
        assertEquals(newQuantity,updated.getQuantity());
        assertEquals(newNote,updated.getNote());
    }

    @Test
    public void create() {
        ProductTypeImpl productType= new ProductTypeImpl();
        productType.setBarCode("123456789012");
        productType.setProductDescription("Pen");
        productType.setPricePerUnit(1.00);
        assertTrue(repo.create(productType)>0);
    }

    @Test
    public void delete() {
        repo.delete(repo.find(39));
        assertNull(repo.find(39));
    }
}