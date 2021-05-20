package it.polito.ezshop.data;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
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
        Transaction transaction = session.beginTransaction();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<BalanceOperationImpl> cr = cb.createQuery(BalanceOperationImpl.class);
            Root<BalanceOperationImpl> root = cr.from(BalanceOperationImpl.class);
            cr.select(root);
            List<Predicate> datePredicates = new ArrayList<>();
            if (from != null) {
                ParameterExpression<LocalDate> fromDate = cb.parameter(LocalDate.class, "fromDate");
                datePredicates.add(cb.greaterThanOrEqualTo(root.get("date"), fromDate));
            }
            if (to != null) {
                ParameterExpression<LocalDate> toDate = cb.parameter(LocalDate.class, "toDate");
                datePredicates.add(cb.lessThanOrEqualTo(root.get("date"), toDate));
            }
            if (!datePredicates.isEmpty()) {
                cr.where(cb.and(datePredicates.toArray(new Predicate[0])));
            }
            Query<BalanceOperationImpl> query = session.createQuery(cr);
            if (from != null) {
                query.setParameter("fromDate", from);
            }
            if (to != null) {
                query.setParameter("toDate", to);
            }
            List<BalanceOperation> balanceOperations = (List<BalanceOperation>)(Object) query.list();
            transaction.commit();
            return balanceOperations;
        } catch (Exception exception) {
            transaction.rollback();
            if (exception instanceof NoResultException) {
                return null;
            }
            throw exception;
        }
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
