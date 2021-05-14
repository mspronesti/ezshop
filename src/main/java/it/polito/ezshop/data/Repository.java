package it.polito.ezshop.data;

import it.polito.ezshop.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;

public abstract class Repository<T> {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    protected T _find(Class<? extends T> entityClass, Serializable id) {
        Session session = getSession();
        session.beginTransaction();
        T entity = session.get(entityClass, id);
        session.getTransaction().commit();
        return entity;
    }

    @SuppressWarnings("unchecked")
    protected List<T> _findAll(Class<? extends T> entityClass) {
        Session session = getSession();
        session.beginTransaction();
        List<T> users = (List<T>) session
                .createQuery("FROM " + entityClass.getName(), entityClass)
                .list();
        session.getTransaction().commit();
        return users;
    }

    protected Serializable _create(T entity) {
        Session session = getSession();
        session.beginTransaction();
        Serializable id = session.save(entity);
        session.getTransaction().commit();
        return id;
    }

    @SuppressWarnings("unchecked")
    protected T _update(T entity) {
        Session session = getSession();
        session.beginTransaction();
        T updatedEntity = (T) session.merge(entity);
        session.getTransaction().commit();
        return updatedEntity;
    }

    protected void _delete(T entity) {
        Session session = getSession();
        session.beginTransaction();
        session.delete(entity);
        session.getTransaction().commit();
    }

    abstract T find(Serializable id);

    abstract List<T> findAll();

    abstract Serializable create(T entity);

    abstract T update(T entity);

    abstract void delete(T entity);
}
