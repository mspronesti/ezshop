package it.polito.ezshop.data;

import java.util.List;

public class UserRepository extends Repository<User> {

    @Override
    User find(Integer id) {
        session.getTransaction().begin();
        User user = session.get(User.class, id);
        session.getTransaction().commit();
        return user;
    }

    @Override
    List<? extends User> findAll() {
        session.getTransaction().begin();
        List<? extends User> users = session.createQuery("FROM UserImpl", UserImpl.class).list();
        session.getTransaction().commit();
        return users;
    }

    @Override
    Integer create(User user) {
        session.getTransaction().begin();
        Integer id = (Integer) session.save(user);
        session.getTransaction().commit();
        return id;
    }

    @Override
    User update(User user) {
        session.getTransaction().begin();
        User updatedUser = (User) session.merge(user);
        session.getTransaction().commit();
        return updatedUser;
    }

    @Override
    void delete(User user) {
        session.getTransaction().begin();
        session.delete(user);
        session.getTransaction().commit();
    }
}
