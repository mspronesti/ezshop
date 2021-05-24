package it.polito.ezshop.data;

import org.junit.Test;

import java.time.LocalDate;
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
        Integer id;
        Integer points=12;
        String cardId="1763985854";
        String name="Giulio";

        id=customerRepository.create(customer);

        customer.setCustomerName(name);
        customer.setCustomerCard(cardId);
        customer.setPoints(points);

        customerRepository.update(customer);

        Customer updated = customerRepository.find(id);

        assertEquals(name, updated.getCustomerName());
        System.out.println(updated.getPoints());
        System.out.println(updated.getCustomerCard());
        //assertTrue(points.equals(updated.getPoints()) || updated.getPoints()==null);
        //assertEquals(balanceId, updated.getBalanceId());
        //assertTrue(updated.getCustomerCard().equals(cardId) || updated.getCustomerCard()==null);

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