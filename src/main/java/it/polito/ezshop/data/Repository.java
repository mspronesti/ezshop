package it.polito.ezshop.data;

import it.polito.ezshop.util.HibernateUtil;
import org.hibernate.Session;
import java.util.List;
import java.util.Optional;

public abstract class Repository<T> {
    protected Session session = HibernateUtil.getSessionFactory().getCurrentSession();

    abstract T find(Integer id);

    abstract List<? extends T> findAll();

    abstract Integer create(T entity);

    abstract T update(T entity);

    abstract void delete(T entity);
}
