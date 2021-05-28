package it.polito.ezshop.data;

import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CustomerRepositoryTest {
    private static final CustomerRepository repo = new CustomerRepository();

    @Test
    public void find() {
        Integer customerId=46;
        assertEquals(customerId,repo.find(customerId).getId());
    }

    @Test
    public void findAll() {
        assertEquals(repo.findAll().getClass(), ArrayList.class);
        assertEquals(repo.findAll().get(0).getClass(), CustomerImpl.class);
    }

    @Test
    public void findByName() {
        String name = "Gennaro";
        assertEquals(name, repo.findByName(name).getCustomerName());
    }


    @Test
    public void update() {
        LoyaltyCardRepository loyaltyCardRepository = new LoyaltyCardRepository();
        LoyaltyCard card= new LoyaltyCardImpl();
        String newCardId=loyaltyCardRepository.create(card);
        int newPoint=312;
        card.setPoints(newPoint);
        loyaltyCardRepository.update(card);

        Customer customer = repo.find(46);

        customer.setCustomerCard(newCardId);
        customer.setPoints(newPoint);

        Customer updated=repo.update(customer);

        assert(newPoint==updated.getPoints());
        assertEquals(newCardId, updated.getCustomerCard());
    }

    @Test
    public void create() {
        assertTrue(repo.create(new CustomerImpl())>0);
    }

    @Test
    public void delete() {
        repo.delete(repo.find(47));
        assertNull(repo.find(47));
    }
}