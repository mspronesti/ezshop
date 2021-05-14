package it.polito.ezshop.data;

import java.io.Serializable;
import java.util.List;

public class SaleTransactionRepository extends Repository<SaleTransaction> {
    @Override
    SaleTransaction find(Serializable id) {
        return _find(SaleTransactionImpl.class, id);
    }

    @Override
    List<SaleTransaction> findAll() {
        return _findAll(SaleTransactionImpl.class);
    }

    @Override
    Integer create(SaleTransaction saleTransaction) {
        return (Integer) _create(saleTransaction);
    }

    @Override
    SaleTransaction update(SaleTransaction saleTransaction) {
        return _update(saleTransaction);
    }

    @Override
    void delete(SaleTransaction saleTransaction) {
        _delete(saleTransaction);
    }
}
