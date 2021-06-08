package it.polito.ezshop.data;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ProductRepositoryTest {
    private static final ProductRepository repo = new ProductRepository();

    @Test
    public void find() {
        ProductImpl product = new ProductImpl();
        String productId = "1001";

        product.setId(productId);
        repo.create(product);

        assertEquals(productId, repo.find(productId).getId());
    }

    @Test
    public void findAll() {

        ProductImpl product = new ProductImpl();
        String productId = "1002";
        ProductImpl product2 = new ProductImpl();
        String productId2 = "1003";

        product.setId(productId);
        product2.setId(productId2);

        repo.create(product);
        repo.create(product2);

        assertEquals(repo.findAll().getClass(), ArrayList.class);
        assertEquals(repo.findAll().get(0).getClass(), ProductImpl.class);
    }

    @Test
    public void create() {
        ProductImpl product = new ProductImpl();
        String productId = "1000";

        product.setId(productId);
        assertTrue(Integer.parseInt(repo.create(product)) > 0);
    }

    @Test
    public void update() {
        ProductImpl product = new ProductImpl();
        String productId = "1005";

        ProductTypeRepository productTypeRepository = new ProductTypeRepository();
        ProductType productType=productTypeRepository.findByBarcode("012345678950");
        productTypeRepository.update(productType);

        product.setId(productId);
        repo.create(product);
        product.setProductType((ProductTypeImpl) productType);
        Product updated = repo.update(product);

        assertEquals(productId, updated.getId());
        assertEquals(productType.getId(), updated.getProductType().getId());
    }

    @Test
    public void delete() {
        {
            ProductImpl product = new ProductImpl();
            String productId = "1004";

            product.setId(productId);
            repo.create(product);
            repo.delete(product);
            assertNull(repo.find(productId));
        }
    }
}