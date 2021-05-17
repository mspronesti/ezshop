package it.polito.ezshop.data;

import java.io.Serializable;
import java.util.List;

public class ReturnTransactionRepository extends Repository<ReturnTransactionImpl> {
    @Override
    ReturnTransactionImpl find(Serializable id) {
        return _find(ReturnTransactionImpl.class, id);
    }

    @Override
    List<ReturnTransactionImpl> findAll() {
        return _findAll(ReturnTransactionImpl.class);
    }

    @Override
    Integer create(ReturnTransactionImpl saleTransaction) {
        return (Integer) _create(saleTransaction);
    }

    @Override
    ReturnTransactionImpl update(ReturnTransactionImpl saleTransaction) {
        return _update(saleTransaction);
    }

    @Override
    void delete(ReturnTransactionImpl saleTransaction) {
        _delete(saleTransaction);
    }
}
