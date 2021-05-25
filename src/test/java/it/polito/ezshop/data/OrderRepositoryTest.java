package it.polito.ezshop.data;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class OrderRepositoryTest {


    private static OrderRepository repo = new OrderRepository();
    private static List<Order> orderList=new ArrayList<>();
    private static OrderImpl order;
    private static Integer orderId;

    @Before
    public void init(){
        order = new OrderImpl();
        order.setBalanceId(43123);
        order.setProductCode("01DA2");
        order.setQuantity(12);
        order.setStatus("PAYED");
        order.setPricePerUnit(13.00);
        orderId=repo.create(order);
    }
    
    @Test
    public void find() {
        assertEquals(orderId,repo.find(orderId).getOrderId());
    }

    @Test
    public void findAll() {
        List<Integer> idArray = new ArrayList<>();
        OrderImpl order2 = new OrderImpl();
        OrderImpl order3 = new OrderImpl();

        orderList.add(order2);
        orderList.add(order3);

        idArray.add(orderId);

        for (Order entry:orderList) {
            idArray.add((repo.create(entry)));
        }

        orderList=repo.findAll();

        for (Order entry:orderList) {
            assertTrue(idArray.contains(entry.getOrderId()));
        }
    }

    @Test
    public void create() {
        assertTrue(orderId>0);
    }

    @Test
    public void update() {
        String newStatus = "COMPLETED";
        Integer newBalanceId = 30;
        String newProductCode = "12DE2";
        double newPrice = 18.50;
        int newQuantity = 12;

        order.setBalanceId(newBalanceId);
        order.setProductCode(newProductCode);
        order.setPricePerUnit(newPrice);
        order.setQuantity(newQuantity);
        order.setStatus(newStatus);

        Order updated=repo.update(order);

        assertEquals(newStatus, updated.getStatus());
        assertEquals(newQuantity, updated.getQuantity());
        assert(newPrice==updated.getPricePerUnit());
        assertEquals(newProductCode, updated.getProductCode());
        assertEquals(newBalanceId, updated.getBalanceId());

        repo.delete(order);
    }

    @Test
    public void delete() {
        repo.delete(order);
        assertNull(repo.find(orderId));
    }

    @After
    public void stop(){
        orderList=repo.findAll();
        for (Order o:orderList) {
            repo.delete(o);
        }
        orderList.clear();
    }
}