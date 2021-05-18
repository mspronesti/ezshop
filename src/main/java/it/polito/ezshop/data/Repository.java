package it.polito.ezshop.data;

import it.polito.ezshop.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.util.List;

public abstract class Repository<T> {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    protected T _find(Class<? extends T> entityClass, Serializable id) {
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        try {
            T entity = session.get(entityClass, id);
            transaction.commit();
            return entity;
        } catch (Exception exception) {
            transaction.rollback();
            throw exception;
        }
    }

    @SuppressWarnings("unchecked")
    protected List<T> _findAll(Class<? extends T> entityClass) {
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        try {
            List<T> users = (List<T>) session
                    .createQuery("FROM " + entityClass.getName(), entityClass)
                    .list();
            transaction.commit();
            return users;
        } catch (Exception exception) {
            transaction.rollback();
            throw exception;
        }
    }

    protected Serializable _create(T entity) {
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        try {
            Serializable id = session.save(entity);
            transaction.commit();
            return id;
        } catch (Exception exception) {
            transaction.rollback();
            throw exception;
        }
    }

    @SuppressWarnings("unchecked")
    protected T _update(T entity) {
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        try {
            T updatedEntity = (T) session.merge(entity);
            transaction.commit();
            return updatedEntity;
        } catch (Exception exception) {
            transaction.rollback();
            throw exception;
        }
    }

    protected void _delete(T entity) {
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.delete(entity);
            transaction.commit();
        } catch (Exception exception) {
            transaction.rollback();
            throw exception;
        }
    }

    abstract T find(Serializable id);

    abstract List<T> findAll();

    abstract Serializable create(T entity);

    abstract T update(T entity);

    abstract void delete(T entity);
}
