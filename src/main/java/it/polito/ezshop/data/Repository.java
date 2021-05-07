package it.polito.ezshop.data;

import it.polito.ezshop.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

public abstract class Repository<T> {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    abstract T find(Integer id);

    abstract List<? extends T> findAll();

    abstract Integer create(T entity);

    abstract T update(T entity);

    abstract void delete(T entity);
}
