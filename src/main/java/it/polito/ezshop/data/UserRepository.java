package it.polito.ezshop.data;

import org.hibernate.Session;

import java.util.List;

public class UserRepository extends Repository<User> {
    @Override
    User find(Integer id) {
        Session session = getSession();
        session.beginTransaction();
        User user = session.get(UserImpl.class, id);
        session.getTransaction().commit();
        return user;
    }

    User findByUsername(String username) {
        Session session = getSession();
        session.beginTransaction();
        User user = session
                .createQuery("FROM UserImpl WHERE username = :username", UserImpl.class)
                .setParameter("username", username)
                .getSingleResult();
        session.getTransaction().commit();
        return user;
    }
    

    @Override
    List<? extends User> findAll() {
        Session session = getSession();
        session.beginTransaction();
        List<? extends User> users = session
                .createQuery("FROM UserImpl", UserImpl.class)
                .list();
        session.getTransaction().commit();
        return users;
    }

    @Override
    Integer create(User user) {
        Session session = getSession();
        session.beginTransaction();
        Integer id = (Integer) session.save(user);
        session.getTransaction().commit();
        return id;
    }

    @Override
    User update(User user) {
        Session session = getSession();
        session.beginTransaction();
        User updatedUser = (User) session.merge(user);
        session.getTransaction().commit();
        return updatedUser;
    }

    @Override
    void delete(User user) {
        Session session = getSession();
        session.beginTransaction();
        session.delete(user);
        session.getTransaction().commit();
    }
}
