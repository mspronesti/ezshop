package it.polito.ezshop.data;

import java.io.Serializable;
import java.util.List;

public class ProductRepository extends Repository<Product> {
    @Override
    Product find(Serializable id) {
        return _find(ProductImpl.class, id);
    }

    @Override
    List<Product> findAll() {
        return _findAll(ProductImpl.class);
    }

    @Override
    String create(Product product) {
        return (String) _create(product);
    }

    @Override
    Product update(Product product) {
        return _update(product);
    }

    @Override
    void delete(Product product) {
        _delete(product);
    }
}
