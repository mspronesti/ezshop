package it.polito.ezshop.data;

import java.io.Serializable;
import java.util.List;

public class CustomerRepository extends Repository<Customer> {
    @Override
    Customer find(Serializable id) {
        return _find(CustomerImpl.class, id);
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
