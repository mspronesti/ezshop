package it.polito.ezshop.data;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CustomerRepositoryTest {

    @Test
    public void find() {
        CustomerRepository customerRepository = new CustomerRepository();
        CustomerImpl customer = new CustomerImpl();
        Integer id;
        id=customerRepository.create(customer);
        assert(id.equals(customerRepository.find(id).getId()));
        customerRepository.delete(customer);
    }

    @Test
    public void findByName() {
        CustomerRepository customerRepository = new CustomerRepository();
        CustomerImpl customer = new CustomerImpl();
        String name="Marco";
        customer.setCustomerName(name);
        int id=customerRepository.create(customer);

        assert(id==customerRepository.findByName(name).getId());
        customerRepository.delete(customer);


    }

    @Test
    public void findAll() {
        CustomerRepository customerRepository = new CustomerRepository();
        CustomerImpl customer1 = new CustomerImpl();
        CustomerImpl customer2 = new CustomerImpl();
        CustomerImpl customer3 = new CustomerImpl();
        List<Customer> CustomerList = new ArrayList<>();
        List<Integer> idArray=new ArrayList<>();


        CustomerList.add(customer1); CustomerList.add(customer2); CustomerList.add(customer3);

        for (Customer entry:CustomerList) {
            idArray.add((customerRepository.create(entry)));
        }

        CustomerList=customerRepository.findAll();


        for (Customer entry:CustomerList) {
            assertTrue(idArray.contains(entry.getId()));
        }

        for (Customer entry:CustomerList) {
            customerRepository.delete(entry);
        }
    }

    @Test
    public void create() {
        CustomerRepository customerRepository = new CustomerRepository();
        Customer customer = new CustomerImpl();

        assertTrue(customerRepository.create(customer)>0);

        customerRepository.delete(customer);
    }

    @Test
    public void update() {
        CustomerRepository customerRepository = new CustomerRepository();
        Customer customer = new CustomerImpl();
        LoyaltyCardRepository loyaltyCardRepository = new LoyaltyCardRepository();
        LoyaltyCardImpl loyaltyCard = new LoyaltyCardImpl();

        Integer customerId;
        Integer points=12;
        String loyaltyCardId = loyaltyCardRepository.create(loyaltyCard);
        String name="Giulio";

        customerId=customerRepository.create(customer);
        customer.setCustomerName(name);
        customer.setCustomerCard(loyaltyCardId);
        customer.setPoints(points);

        Customer customer1 = customerRepository.update(customer);

        assertEquals(customerId,customerRepository.find(customerId).getId());
        assertEquals(name,customer1.getCustomerName());
        assertEquals(loyaltyCardId,customer1.getCustomerCard());
        //assertEquals(points,customer1.getPoints());

        loyaltyCardRepository.delete(loyaltyCard);
        customerRepository.delete(customer);
    }

    @Test
    public void delete() {
        CustomerRepository customerRepository = new CustomerRepository();
        Customer customer = new CustomerImpl();
        Integer id;

        id=customerRepository.create(customer);
        customerRepository.delete(customer);
        assertNull(customerRepository.find(id));
    }
}