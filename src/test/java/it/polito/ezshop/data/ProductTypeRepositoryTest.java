package it.polito.ezshop.data;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ProductTypeRepositoryTest {
    
    private static ProductTypeRepository repo = new ProductTypeRepository();
    private static List<ProductType> productTypeList=new ArrayList<>();
    private static ProductTypeImpl productType;
    private static Integer productTypeId;
    private static String productTypeBarcode="012345678905";

    @Before
    public void init(){
        productType = new ProductTypeImpl();
        productType.setBarCode(productTypeBarcode);
        productType.setProductDescription("T-shirt");
        productType.setNote("Blue");
        productType.setQuantity(12);
        productType.setPricePerUnit(2.50);
        productType.setLocation("3-a-12");
        productTypeId=repo.create(productType);
    }


    @Test
    public void find() {
        assertEquals(productTypeId,repo.find(productTypeId).getId());
    }

    @Test
    public void findByBarcode() {
        assertEquals(productTypeBarcode,repo.findByBarcode(productTypeBarcode).getBarCode());
    }

    @Test
    public void findAll() {
        List<Integer> idArray = new ArrayList<>();
        ProductTypeImpl productType2 = new ProductTypeImpl();
        productType2.setBarCode("978073562153");
        ProductTypeImpl productType3 = new ProductTypeImpl();
        productType3.setBarCode("7891234567897");

        productTypeList.add(productType2);
        productTypeList.add(productType3);

        idArray.add(productTypeId);

        for (ProductType entry:productTypeList) {
            idArray.add((repo.create(entry)));
        }

        productTypeList=repo.findAll();

        for (ProductType entry:productTypeList) {
            assertTrue(idArray.contains(entry.getId()));
        }

    }

    @Test
    public void create() {
        assertTrue(productTypeId>0);
    }

    @Test
    public void update() {
        Integer newQuantity=4;
        String newLocation = "3-a-15";
        String newNote = "Yellow";
        String newDescription = "Shirt";
        String newBarcode = "13148419";
        double newPrice = 15.24;

        productType.setQuantity(newQuantity);
        productType.setLocation(newLocation);
        productType.setNote(newNote);
        productType.setProductDescription(newDescription);
        productType.setBarCode(newBarcode);
        productType.setPricePerUnit(newPrice);

        ProductType updated = repo.update(productType);

        assertEquals(newQuantity,updated.getQuantity());
        assertEquals(newLocation,updated.getLocation());
        assertEquals(newNote,updated.getNote());
        assertEquals(newDescription,updated.getProductDescription());
        assertEquals(newBarcode,updated.getBarCode());
        assert(newPrice == updated.getPricePerUnit());
    }
    
    @Test
    public void delete() {
        repo.delete(productType);
        assertNull(repo.find(productTypeId));
    }

    @After
    public void stop(){
        productTypeList=repo.findAll();
        for (ProductType p:productTypeList) {
            repo.delete(p);
        }
        productTypeList.clear();
    }
}