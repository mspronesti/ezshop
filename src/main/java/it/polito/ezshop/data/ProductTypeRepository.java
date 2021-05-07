package it.polito.ezshop.data;

import org.hibernate.Session;

import java.util.List;

public class ProductTypeRepository extends Repository<ProductType> {
    @Override
    ProductType find(Integer id) {
        Session session = getSession();
        session.beginTransaction();
        ProductType productType = session.get(ProductTypeImpl.class, id);
        session.getTransaction().commit();
        return productType;
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
    List<? extends ProductType> findAll() {
        Session session = getSession();
        session.beginTransaction();
        List<? extends ProductType> productTypes = session
                .createQuery("FROM ProductTypeImpl", ProductTypeImpl.class)
                .list();
        session.getTransaction().commit();
        return productTypes;
    }

    @Override
    Integer create(ProductType productType) {
        Session session = getSession();
        session.beginTransaction();
        Integer id = (Integer) session.save(productType);
        session.getTransaction().commit();
        return id;
    }

    @Override
    ProductType update(ProductType productType) {
        Session session = getSession();
        session.beginTransaction();
        ProductType updatedProductType = (ProductType) session.merge(productType);
        session.getTransaction().commit();
        return updatedProductType;
    }

    @Override
    void delete(ProductType productType) {
        Session session = getSession();
        session.beginTransaction();
        session.delete(productType);
        session.getTransaction().commit();
    }
}
