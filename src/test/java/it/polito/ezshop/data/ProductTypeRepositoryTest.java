package it.polito.ezshop.data;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ProductTypeRepositoryTest {

    @Test
    public void find() {
        ProductTypeRepository productTypeRepository = new ProductTypeRepository();
        ProductTypeImpl productType = new ProductTypeImpl();
        productType.setBarCode("012345678905");
        Integer id = productTypeRepository.create(productType);
        assertEquals(id,productTypeRepository.find(id).getId());
        productTypeRepository.delete(productType);
    }

    @Test
    public void findByBarcode() {
        ProductTypeRepository productTypeRepository = new ProductTypeRepository();
        ProductTypeImpl productType = new ProductTypeImpl();


        String barcode = "978073562153";
        productType.setBarCode(barcode);
        Integer id = productTypeRepository.create(productType);
        assertEquals(id,productTypeRepository.findByBarcode(productType.getBarCode()).getId());
        productTypeRepository.delete(productType);
    }

    @Test
    public void findAll() {
        ProductTypeRepository productTypeRepository = new ProductTypeRepository();
        List<ProductType> productTypeList = new ArrayList<>();
        List<Integer> idArray = new ArrayList<>();

        ProductTypeImpl productType1 = new ProductTypeImpl();
        productType1.setBarCode("012345678905");
        ProductTypeImpl productType2 = new ProductTypeImpl();
        productType2.setBarCode("978073562153");
        ProductTypeImpl productType3 = new ProductTypeImpl();
        productType3.setBarCode("9781234567897");

        productTypeList.add(productType1);
        productTypeList.add(productType2);
        productTypeList.add(productType3);

        for (ProductType entry:productTypeList){
            idArray.add(productTypeRepository.create(entry));
        }

        productTypeList = productTypeRepository.findAll();

        for (ProductType entry:productTypeList){
            assertTrue(idArray.contains(entry.getId()));
        }

        for (ProductType entry:productTypeList){
            productTypeRepository.delete(entry);
        }
    }

    @Test
    public void create() {
        ProductTypeRepository productTypeRepository = new ProductTypeRepository();
        ProductTypeImpl productType = new ProductTypeImpl();
        assertTrue(productTypeRepository.create(productType)>0);
        productTypeRepository.delete(productType);
    }

    @Test
    public void update() {
        ProductTypeRepository productTypeRepository = new ProductTypeRepository();
        ProductTypeImpl productType = new ProductTypeImpl();
        Integer id = productTypeRepository.create(productType);

        Integer quantity=4;
        productType.setQuantity(quantity);

        String location = "3-a-12";
        productType.setLocation(location);

        String note = "Blue version";
        productType.setNote(note);

        String description = "Blue t-shirt";
        productType.setProductDescription(description);

        String barcode = "13148419";
        productType.setBarCode(barcode);

        double price = 15.24;
        productType.setPricePerUnit(price);

        ProductType productType1 = productTypeRepository.update(productType);

        assertEquals(quantity,productType1.getQuantity());
        assertEquals(location,productType1.getLocation());
        assertEquals(note,productType1.getNote());
        assertEquals(description,productType1.getProductDescription());
        assertEquals(barcode,productType1.getBarCode());
        assert(price == productType1.getPricePerUnit());
        productTypeRepository.delete(productType);

    }

    @Test
    public void delete() {
        ProductTypeRepository productTypeRepository = new ProductTypeRepository();
        ProductTypeImpl productType = new ProductTypeImpl();
        Integer id = productTypeRepository.create(productType);
        productTypeRepository.delete(productType);
        assertNull(productTypeRepository.find(id));
    }
}