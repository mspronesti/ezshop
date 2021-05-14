package it.polito.ezshop.data;

import org.hibernate.Session;

import java.io.Serializable;
import java.util.List;

public class ProductTypeRepository extends Repository<ProductType> {
    @Override
    ProductType find(Serializable id) {
        return _find(ProductTypeImpl.class, id);
    }

    ProductType findByBarcode(String barcode) {
        Session session = getSession();
        session.beginTransaction();
        ProductType productType = session
                .createQuery("FROM ProductTypeImpl WHERE barcode = :barcode", ProductTypeImpl.class)
                .setParameter("barcode", barcode)
                .getSingleResult();
        session.getTransaction().commit();
        return productType;
    }

    @Override
    List<ProductType> findAll() {
        return _findAll(ProductTypeImpl.class);
    }

    @Override
    Integer create(ProductType productType) {
        return (Integer) _create(productType);
    }

    @Override
    ProductType update(ProductType productType) {
        return _update(productType);
    }

    @Override
    void delete(ProductType productType) {
        _delete(productType);
    }
}
