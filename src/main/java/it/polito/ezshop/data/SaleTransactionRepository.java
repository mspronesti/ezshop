package it.polito.ezshop.data;

import org.hibernate.Session;

import java.util.List;

public class SaleTransactionRepository extends Repository<SaleTransaction> {
    @Override
    SaleTransaction find(Integer id) {
        Session session = getSession();
        session.beginTransaction();
        SaleTransaction saleTransaction = session.get(SaleTransactionImpl.class, id);
        session.getTransaction().commit();
        return saleTransaction;
    }

    @Override
    List<? extends SaleTransaction> findAll() {
        Session session = getSession();
        session.beginTransaction();
        List<? extends SaleTransaction> saleTransactions = session
                .createQuery("FROM SaleTransactionImpl", SaleTransactionImpl.class)
                .list();
        session.getTransaction().commit();
        return saleTransactions;
    }

    @Override
    Integer create(SaleTransaction saleTransaction) {
        Session session = getSession();
        session.beginTransaction();
        Integer id = (Integer) session.save(saleTransaction);
        session.getTransaction().commit();
        return id;
    }

    @Override
    SaleTransaction update(SaleTransaction saleTransaction) {
        Session session = getSession();
        session.beginTransaction();
        SaleTransaction updatedSaleTransaction = (SaleTransaction) session.merge(saleTransaction);
        session.getTransaction().commit();
        return updatedSaleTransaction;
    }

    @Override
    void delete(SaleTransaction saleTransaction) {
        Session session = getSession();
        session.beginTransaction();
        session.delete(saleTransaction);
        session.getTransaction().commit();
    }
}
