package it.polito.ezshop.data;

import org.hibernate.Session;

import java.util.List;

public class OrderRepository extends Repository<Order> {
    @Override
    Order find(Integer id) {
        Session session = getSession();
        session.beginTransaction();
        Order order = session.get(OrderImpl.class, id);
        session.getTransaction().commit();
        return order;
    }

    @Override
    List<? extends Order> findAll() {
        Session session = getSession();
        session.beginTransaction();
        List<? extends Order> orders = session
                .createQuery("FROM OrderImpl", OrderImpl.class)
                .list();
        session.getTransaction().commit();
        return orders;
    }

    @Override
    Integer create(Order order) {
        Session session = getSession();
        session.beginTransaction();
        Integer id = (Integer) session.save(order);
        session.getTransaction().commit();
        return id;
    }

    @Override
    Order update(Order order) {
        Session session = getSession();
        session.beginTransaction();
        Order updatedOrder = (Order) session.merge(order);
        session.getTransaction().commit();
        return updatedOrder;
    }

    @Override
    void delete(Order order) {
        Session session = getSession();
        session.beginTransaction();
        session.delete(order);
        session.getTransaction().commit();
    }
}
