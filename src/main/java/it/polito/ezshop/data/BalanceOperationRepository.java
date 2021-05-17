package it.polito.ezshop.data;

import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
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
        try {
            Session session = getSession();
            session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<BalanceOperationImpl> cr = cb.createQuery(BalanceOperationImpl.class);
            Root<BalanceOperationImpl> root = cr.from(BalanceOperationImpl.class);
            cr.select(root);
            if (from != null) {
                ParameterExpression<LocalDate> fromDate = cb.parameter(LocalDate.class, "fromDate");
                cr.where(cb.greaterThanOrEqualTo(root.get("date"), fromDate));
            }
            if (to != null) {
                ParameterExpression<LocalDate> toDate = cb.parameter(LocalDate.class, "toDate");
                cr.where(cb.lessThanOrEqualTo(root.get("date"), toDate));
            }
            List<BalanceOperation> balanceOperations = (List<BalanceOperation>)(Object) session.createQuery(cr).list();
            session.getTransaction().commit();
            return balanceOperations;
        } catch (Exception exception) {
            return null;
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
