package it.polito.ezshop.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class CustomerRepository extends Repository<Customer> {
    @Override
    Customer find(Serializable id) {
        return _find(CustomerImpl.class, id);
    }
    
    Customer findByName(String name) {
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        try {
            Customer customer = session
                    .createQuery("FROM CustomerImpl WHERE name = :name", CustomerImpl.class)
                    .setParameter("name", name)
                    .getSingleResult();
            transaction.commit();
            return customer;
        } catch (Exception exception) {
            transaction.rollback();
            if (exception instanceof NoResultException) {
                return null;
            }
            throw exception;
        }
    }

    @Override
    List<Customer> findAll() {
        return _findAll(CustomerImpl.class);
    }

    @Override
    Integer create(Customer customer) {
        return (Integer) _create(customer);
    }

    @Override
    Customer update(Customer customer) {
        return _update(customer);
    }

    @Override
    void delete(Customer customer) {
        _delete(customer);
    }
}
