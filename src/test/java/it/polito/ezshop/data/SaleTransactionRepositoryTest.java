package it.polito.ezshop.data;


import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SaleTransactionRepositoryTest {




    @Test
    public void find() {
        SaleTransactionImpl saleTr = new SaleTransactionImpl();
        SaleTransactionRepository repo= new SaleTransactionRepository();
        Integer id= repo.create(saleTr);
        assertEquals(id,repo.find(id).getTicketNumber());
        repo.delete(saleTr);
    }

    @Test
    public void findAll() {
        SaleTransactionRepository repo = new SaleTransactionRepository();
        List<SaleTransaction> saleTransactionList = new ArrayList<>();
        
        SaleTransactionImpl tr1 = new SaleTransactionImpl();
        SaleTransactionImpl tr2 = new SaleTransactionImpl();
        SaleTransactionImpl tr3 = new SaleTransactionImpl();
        repo.create(tr1);
        repo.create(tr2);
        repo.create(tr3);
        
        saleTransactionList.add(tr1);
        saleTransactionList.add(tr2);
        saleTransactionList.add(tr3);
        
        assertEquals(saleTransactionList,repo.findAll());
        repo.delete(tr1);
        repo.delete(tr2);
        repo.delete(tr3);
    }

    @Test
    public void create() {
        SaleTransactionImpl saleTr = new SaleTransactionImpl();
        SaleTransactionRepository repo= new SaleTransactionRepository();
        Integer id= repo.create(saleTr);
        assertEquals(id,saleTr.getTicketNumber());
        repo.delete(saleTr);
    }

    @Test
    public void update() {
        SaleTransactionImpl saleTr = new SaleTransactionImpl();
        SaleTransactionRepository repo= new SaleTransactionRepository();
        BalanceOperationImpl balanceOp = new BalanceOperationImpl();
        List<TicketEntry> ticketList = new ArrayList<>();

        TicketEntryImpl tk1 = new TicketEntryImpl();
        TicketEntryImpl tk2 = new TicketEntryImpl();

        double price = 50;
        double discountRate = 12.50;

        Integer id= repo.create(saleTr);
        saleTr.setDiscountRate(discountRate);
        saleTr.setPrice(price);
        saleTr.setEntries(ticketList);
        saleTr.setPayment(balanceOp);

        ticketList.add(tk1);
        ticketList.add(tk2);
        saleTr.setEntries(ticketList);

        repo.update(saleTr);

        assertEquals(id,repo.find(id).getTicketNumber());
        assertEquals(balanceOp, saleTr.getPayment());
        assert(price == saleTr.getPrice());
        assert(discountRate == saleTr.getDiscountRate());
        assertEquals(ticketList, saleTr.getEntries());
        repo.delete(saleTr);

    }


    @Test
    public void delete() {
        SaleTransactionImpl saleTr = new SaleTransactionImpl();
        SaleTransactionRepository repo= new SaleTransactionRepository();
        Integer id= repo.create(saleTr);
        repo.delete(saleTr);
        assertNull(repo.find(id));
    }
}