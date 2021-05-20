package it.polito.ezshop.data;

import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import java.io.Serializable;
import java.util.List;

public class UserRepository extends Repository<User> {
    @Override
    User find(Serializable id) {
        return _find(UserImpl.class, id);
    }

    User findByUsername(String username) {
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        try {
            User user = session
                    .createQuery("FROM UserImpl WHERE username = :username", UserImpl.class)
                    .setParameter("username", username)
                    .getSingleResult();
            transaction.commit();
            return user;
        } catch (Exception exception) {
            transaction.rollback();
            if (exception instanceof NoResultException) {
                return null;
            }
            throw exception;
        }
    }

    @Override
    List<User> findAll() {
        return _findAll(UserImpl.class);
    }

    @Override
    Integer create(User user) {
        return (Integer) _create(user);
    }

    @Override
    User update(User user) {
        return _update(user);
    }

    @Override
    void delete(User user) {
        _delete(user);
    }
}
