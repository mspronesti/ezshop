package it.polito.ezshop.data;

import org.hibernate.Session;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class BalanceOperationRepository extends Repository<BalanceOperation> {
    @Override
    BalanceOperation find(Serializable id) {
        return _find(BalanceOperationImpl.class, id);
    }

    @Override
    List<BalanceOperation> findAll() {
        return _findAll(BalanceOperationImpl.class);
    }

    @SuppressWarnings("unchecked")
    List<BalanceOperation> findAllBetweenDates(LocalDate from, LocalDate to) {
        Session session = getSession();
        session.beginTransaction();
        List<BalanceOperation> balanceOperations = (List<BalanceOperation>)(Object) session
                .createQuery("FROM BalanceOperationImpl WHERE date > :fromDate AND date < :toDate", BalanceOperationImpl.class)
                .setParameter("fromDate", from)
                .setParameter("toDate", to)
                .list();
        session.getTransaction().commit();
        return balanceOperations;
    }

    @Override
    Integer create(BalanceOperation balanceOperation) {
        return (Integer) _create(balanceOperation);
    }

    @Override
    BalanceOperation update(BalanceOperation balanceOperation) {
        return _update(balanceOperation);
    }

    @Override
    void delete(BalanceOperation balanceOperation) {
        _delete(balanceOperation);
    }
}
