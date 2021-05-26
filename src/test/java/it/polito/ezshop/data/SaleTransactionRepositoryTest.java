package it.polito.ezshop.data;


import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SaleTransactionRepositoryTest {
    private static final SaleTransactionRepository repo = new SaleTransactionRepository();

    @Test
    public void find() {
        Integer saleTransactionId=52;
        assertEquals(saleTransactionId,repo.find(saleTransactionId).getTicketNumber());
    }

    @Test
    public void findAll() {
        assertEquals(ArrayList.class, repo.findAll().getClass());
        assertTrue(repo.findAll().get(0).getClass().equals(SaleTransaction.class)||
                repo.findAll().get(0).getClass().equals(ReturnTransactionImpl.class));
    }
    @Test
    public void update() {
        List<TicketEntry> ticketList = new ArrayList<>();
        List<String> idArray=new ArrayList<>();


        TicketEntryImpl tk1 = new TicketEntryImpl();
        TicketEntryImpl tk2 = new TicketEntryImpl();
        String barcode1="012345678905";
        String barcode2="012345678936";
        tk1.setBarCode(barcode1);
        tk2.setBarCode(barcode2);
        ticketList.add(tk1);
        ticketList.add(tk2);

        for (TicketEntry entry:ticketList) {
            idArray.add(entry.getBarCode());
        }

        double newPrice = 50;
        double newDiscountRate = 12.50;

        SaleTransaction saleTransaction = repo.find(52);
        saleTransaction.setDiscountRate(newDiscountRate);
        saleTransaction.setPrice(newPrice);
        saleTransaction.setEntries(ticketList);


        saleTransaction.setEntries(ticketList);
        SaleTransaction updated = repo.update(saleTransaction);

        assert(newPrice == updated.getPrice());
        assert(newDiscountRate == updated.getDiscountRate());
        for (TicketEntry entry:updated.getEntries()) {
            assertTrue(idArray.contains(entry.getBarCode()));
        }
    }


 @Test
    public void create() {
        assertTrue(repo.create(new SaleTransactionImpl())>0);
    }

    @Test
    public void delete() {
        repo.delete(repo.find(54));
        assertNull(repo.find(54));
    }

}