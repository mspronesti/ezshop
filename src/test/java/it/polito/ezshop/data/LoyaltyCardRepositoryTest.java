package it.polito.ezshop.data;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LoyaltyCardRepositoryTest {

    @Test
    public void find() {
        LoyaltyCardImpl loyaltyCard= new LoyaltyCardImpl();
        LoyaltyCardRepository loyaltyCardRepository = new LoyaltyCardRepository();
        String id = loyaltyCardRepository.create(loyaltyCard);
        assertEquals(id,loyaltyCardRepository.find(id).getId());
        loyaltyCardRepository.delete(loyaltyCard);
    }

    @Test
    public void findAll() {
        LoyaltyCardRepository loyaltyCardRepository = new LoyaltyCardRepository();
        List<LoyaltyCard> loyaltyCardList = new ArrayList<>();
        List<String> idArray = new ArrayList<>();

        LoyaltyCardImpl loyaltyCard1= new LoyaltyCardImpl();
        LoyaltyCardImpl loyaltyCard2= new LoyaltyCardImpl();
        LoyaltyCardImpl loyaltyCard3= new LoyaltyCardImpl();


        loyaltyCardList.add(loyaltyCard1);
        loyaltyCardList.add(loyaltyCard2);
        loyaltyCardList.add(loyaltyCard3);

        for (LoyaltyCard entry:loyaltyCardList) {
            idArray.add((loyaltyCardRepository.create(entry)));
        }

        loyaltyCardList=loyaltyCardRepository.findAll();


        for (LoyaltyCard entry:loyaltyCardList) {
            assertTrue(idArray.contains(entry.getId()));
        }

        for (LoyaltyCard entry:loyaltyCardList) {
            loyaltyCardRepository.delete(entry);
        }

    }

    @Test
    public void create() {
        LoyaltyCardImpl loyaltyCard= new LoyaltyCardImpl();
        LoyaltyCardRepository loyaltyCardRepository = new LoyaltyCardRepository();
        String id = loyaltyCardRepository.create(loyaltyCard);
        assertEquals(id,loyaltyCard.getId());
        loyaltyCardRepository.delete(loyaltyCard);
    }

    @Test
    public void update() {
        LoyaltyCardImpl loyaltyCard= new LoyaltyCardImpl();
        LoyaltyCardRepository loyaltyCardRepository = new LoyaltyCardRepository();
        //CustomerImpl customer = new CustomerImpl();
        //CustomerRepository customerRepository = new CustomerRepository();

        String id = loyaltyCardRepository.create(loyaltyCard);

        //customer.setId(customerRepository.create(customer));

        Integer points = 30;
        loyaltyCard.setPoints(points);
        //loyaltyCard.setCustomer(customer);
        LoyaltyCard loyaltyCard1 = loyaltyCardRepository.update(loyaltyCard);

        assertEquals(id,loyaltyCard1.getId());
        assertEquals(points,loyaltyCard1.getPoints());
        //assertEquals(customer.getId(),loyaltyCard1.getCustomer().getId());
        //customerRepository.delete(customer);
        loyaltyCardRepository.delete(loyaltyCard);
    }

    @Test
    public void delete() {
        LoyaltyCardImpl loyaltyCard= new LoyaltyCardImpl();
        LoyaltyCardRepository loyaltyCardRepository = new LoyaltyCardRepository();
        String id = loyaltyCardRepository.create(loyaltyCard);
        loyaltyCardRepository.delete(loyaltyCard);
        assertNull(loyaltyCardRepository.find(id));
    }
}