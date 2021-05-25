package it.polito.ezshop.data;

import org.junit.Test;

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
        CustomerImpl customer = new CustomerImpl();
        CustomerRepository customerRepository = new CustomerRepository();

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