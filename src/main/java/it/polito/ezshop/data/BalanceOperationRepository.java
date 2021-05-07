package it.polito.ezshop.data;

import org.hibernate.Session;

import java.time.LocalDate;
import java.util.List;

public class BalanceOperationRepository extends Repository<BalanceOperation> {
    @Override
    BalanceOperation find(Integer id) {
        Session session = getSession();
        session.beginTransaction();
        BalanceOperation balanceOperation = session.get(BalanceOperationImpl.class, id);
        session.getTransaction().commit();
        return balanceOperation;
    }

    @Override
    List<? extends BalanceOperation> findAll() {
        Session session = getSession();
        session.beginTransaction();
        List<? extends BalanceOperation> balanceOperations = session
                .createQuery("FROM BalanceOperationImpl", BalanceOperationImpl.class)
                .list();
        session.getTransaction().commit();
        return balanceOperations;
    }

    List<? extends BalanceOperation> findAllBetweenDates(LocalDate from, LocalDate to) {
        Session session = getSession();
        session.beginTransaction();
        List<? extends BalanceOperation> balanceOperations = session
                .createQuery("FROM BalanceOperationImpl WHERE date > :fromDate AND date < :toDate", BalanceOperationImpl.class)
                .setParameter("fromDate", from)
                .setParameter("toDate", to)
                .list();
        session.getTransaction().commit();
        return balanceOperations;
    }

    @Override
    Integer create(BalanceOperation balanceOperation) {
        Session session = getSession();
        session.beginTransaction();
        Integer id = (Integer) session.save(balanceOperation);
        session.getTransaction().commit();
        return id;
    }

    @Override
    BalanceOperation update(BalanceOperation balanceOperation) {
        Session session = getSession();
        session.beginTransaction();
        BalanceOperation updatedOperation = (BalanceOperation) session.merge(balanceOperation);
        session.getTransaction().commit();
        return updatedOperation;
    }

    @Override
    void delete(BalanceOperation balanceOperation) {
        Session session = getSession();
        session.beginTransaction();
        session.delete(balanceOperation);
        session.getTransaction().commit();
    }
}
