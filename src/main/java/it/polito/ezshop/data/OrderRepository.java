package it.polito.ezshop.data;

import java.io.Serializable;
import java.util.List;

public class OrderRepository extends Repository<Order> {
    @Override
    Order find(Serializable id) {
        return _find(OrderImpl.class, id);
    }

    @Override
    List<Order> findAll() {
        return _findAll(OrderImpl.class);
    }

    @Override
    Integer create(Order order) {
        return (Integer) _create(order);
    }

    @Override
    Order update(Order order) {
        return _update(order);
    }

    @Override
    void delete(Order order) {
        _delete(order);
    }
}
