package it.polito.ezshop.data;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class OrderRepositoryTest {
    private static final OrderRepository repo = new OrderRepository();

    @Test
    public void find() {
        Integer orderId=40;
        assertEquals(orderId,repo.find(orderId).getOrderId());
    }

    @Test
    public void findAll() {
        assertEquals(repo.findAll().getClass(), ArrayList.class);
        assertEquals(repo.findAll().get(0).getClass(), OrderImpl.class);
    }

    @Test
    public void update() {
        Integer newBalanceId = 14;
        int newQuantity = 12;
        double newPrice = 11.99;
        String newStatus="PAYED";

        Order order = repo.find(40);

        order.setBalanceId(newBalanceId);
        order.setQuantity(newQuantity);
        order.setPricePerUnit(newPrice);
        order.setStatus(newStatus);

        Order updated=repo.update(order);

        assertEquals(newBalanceId, updated.getBalanceId());
        assertEquals(newQuantity, updated.getQuantity());
        assert(newPrice==updated.getPricePerUnit());
        assertEquals(newStatus, updated.getStatus());

    }

    @Test
    public void create() {
        assertTrue(repo.create(new OrderImpl())>0);
    }

    @Test
    public void delete() {
        repo.delete(repo.find(43));
        assertNull(repo.find(43));
    }
}