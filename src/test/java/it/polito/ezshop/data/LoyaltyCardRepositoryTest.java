package it.polito.ezshop.data;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LoyaltyCardRepositoryTest {
    private static final LoyaltyCardRepository repo = new LoyaltyCardRepository();

    @Test
    public void find() {
        String loyaltyCardId="4635815042";
        assertEquals(loyaltyCardId,repo.find(loyaltyCardId).getId());
    }

    @Test
    public void findAll() {
        assertEquals(repo.findAll().getClass(), ArrayList.class);
        assertEquals(repo.findAll().get(0).getClass(), LoyaltyCardImpl.class);
    }

    @Test
    public void update() {
        CustomerRepository customerRepository = new CustomerRepository();
        Customer newCustomer=customerRepository.find(17);
        Integer customerId=17;
        int newPoint=312;
        LoyaltyCard loyaltyCard = repo.find("8477972267");

        loyaltyCard.setCustomer(newCustomer);
        loyaltyCard.setPoints(newPoint);

        LoyaltyCard updated=repo.update(loyaltyCard);

        assert(newPoint==updated.getPoints());
        assertEquals(customerId, updated.getCustomer().getId());
    }

    @Test
    public void create() {
        assertTrue(repo.create(new LoyaltyCardImpl()).length()>0);
    }

    @Test
    public void delete() {
        String id ="6419247644";
        repo.delete(repo.find(id));
        assertNull(repo.find(id));
    }
}