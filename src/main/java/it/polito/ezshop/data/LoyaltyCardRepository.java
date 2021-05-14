package it.polito.ezshop.data;

import java.io.Serializable;
import java.util.List;

public class LoyaltyCardRepository extends Repository<LoyaltyCard> {
    @Override
    LoyaltyCard find(Serializable id) {
        return _find(LoyaltyCardImpl.class, id);
    }

    @Override
    List<LoyaltyCard> findAll() {
        return _findAll(LoyaltyCardImpl.class);
    }

    @Override
    String create(LoyaltyCard entity) {
        return (String) _create(entity);
    }

    @Override
    LoyaltyCard update(LoyaltyCard entity) {
        return _update(entity);
    }

    @Override
    void delete(LoyaltyCard entity) {
        _delete(entity);
    }
}
