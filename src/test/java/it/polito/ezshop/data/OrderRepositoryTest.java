package it.polito.ezshop.data;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class OrderRepositoryTest {

    @Test
    public void find() {
        OrderImpl order = new OrderImpl();
        OrderRepository orderRepository = new OrderRepository();
        Integer id = orderRepository.create(order);
        assertEquals(id,orderRepository.find(id).getOrderId());
        orderRepository.delete(order);
    }

    @Test
    public void findAll() {
        OrderRepository orderRepository = new OrderRepository();
        List<Order> orderList = new ArrayList<>();
        List<Integer> idArray=new ArrayList<>();
        
        OrderImpl tr1 = new OrderImpl();
        OrderImpl tr2 = new OrderImpl();
        OrderImpl tr3 = new OrderImpl();
        orderRepository.create(tr1);
        orderRepository.create(tr2);
        orderRepository.create(tr3);

        orderList.add(tr1);
        orderList.add(tr2);
        orderList.add(tr3);

        for (Order entry:orderList) {
            idArray.add((orderRepository.create(entry)));
        }

        orderList=orderRepository.findAll();


        for (Order entry:orderList) {
            assertTrue(idArray.contains(entry.getOrderId()));
        }

        for (Order entry:orderList) {
            orderRepository.delete(entry);
        }
    }

    @Test
    public void create() {
        OrderImpl order = new OrderImpl();
        OrderRepository orderRepository = new OrderRepository();
        Integer id = orderRepository.create(order);
        assertEquals(id,order.getOrderId());
        orderRepository.delete(order);
    }

    @Test
    public void update() {
        OrderImpl order = new OrderImpl();
        OrderRepository orderRepository = new OrderRepository();

        Integer id = orderRepository.create(order);
        String status = "PAYED";
        Integer balanceId = 30;
        String productCode = "01DA2";
        double price = 12.50;
        int quantity = 6;

        order.setBalanceId(balanceId);
        order.setProductCode(productCode);
        order.setPricePerUnit(price);
        order.setQuantity(quantity);
        order.setStatus(status);

        orderRepository.update(order);

        assertEquals(id,orderRepository.find(id).getOrderId());
        assertEquals(status, order.getStatus());
        assertEquals(quantity, order.getQuantity());
        assert(price==order.getPricePerUnit());
        assertEquals(productCode, order.getProductCode());
        assertEquals(balanceId, order.getBalanceId());

        orderRepository.delete(order);
    }

    @Test
    public void delete() {
        OrderImpl order = new OrderImpl();
        OrderRepository orderRepository = new OrderRepository();
        Integer id = orderRepository.create(order);
        orderRepository.delete(order);
        assertNull(orderRepository.find(id));
    }
}