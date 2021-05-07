package it.polito.ezshop.data;

import org.hibernate.Session;

import java.util.List;

public class CustomerRepository extends Repository<Customer> {
    @Override
    Customer find(Integer id) {
        Session session = getSession();
        session.beginTransaction();
        Customer customer = session.get(CustomerImpl.class, id);
        session.getTransaction().commit();
        return customer;
    }

    @Override
    List<? extends Customer> findAll() {
        Session session = getSession();
        session.beginTransaction();
        List<? extends Customer> customers = session
                .createQuery("FROM CustomerImpl", CustomerImpl.class)
                .list();
        session.getTransaction().commit();
        return customers;
    }

    @Override
    Integer create(Customer customer) {
        Session session = getSession();
        session.beginTransaction();
        Integer id = (Integer) session.save(customer);
        session.getTransaction().commit();
        return id;
    }

    @Override
    Customer update(Customer customer) {
        Session session = getSession();
        session.beginTransaction();
        Customer updatedCustomer = (Customer) session.merge(customer);
        session.getTransaction().commit();
        return updatedCustomer;
    }

    @Override
    void delete(Customer customer) {
        Session session = getSession();
        session.beginTransaction();
        session.delete(customer);
        session.getTransaction().commit();
    }
}
