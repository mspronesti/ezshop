package it.polito.ezshop.data;

import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ReturnTransactionRepositoryTest {
    private static final ReturnTransactionRepository repo = new ReturnTransactionRepository();

    @Test
    public void find() {
        Integer returnTransactionId=33;
        assertEquals(returnTransactionId,repo.find(returnTransactionId).getTicketNumber());
    }

    @Test
    public void findAll() {
        assertEquals(repo.findAll().getClass(), ArrayList.class);
        assertEquals(repo.findAll().get(0).getClass(), ReturnTransactionImpl.class);
    }

    @Test
    public void update() {
        BalanceOperationImpl newBalanceOp = new BalanceOperationImpl();
        BalanceOperationRepository balanceOperationRepository = new BalanceOperationRepository();
        SaleTransactionImpl newSaleTransaction = new SaleTransactionImpl();
        SaleTransactionRepository saleTransactionRepository = new SaleTransactionRepository();

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
        int newBalanceId = balanceOperationRepository.create(newBalanceOp);
        Integer newTicketNumber = saleTransactionRepository.create(newSaleTransaction);

        ReturnTransactionImpl returnTransaction = repo.find(33);
        returnTransaction.setDiscountRate(newDiscountRate);
        returnTransaction.setPrice(newPrice);
        returnTransaction.setEntries(ticketList);
        returnTransaction.setPayment(newBalanceOp);
        returnTransaction.setSaleTransaction(newSaleTransaction);

        ReturnTransactionImpl updated = repo.update(returnTransaction);

        assert(newPrice == updated.getPrice());
        assert(newDiscountRate == updated.getDiscountRate());
        assertEquals(newBalanceId, updated.getPayment().getBalanceId());
        assertEquals(newTicketNumber, updated.getSaleTransaction().getTicketNumber());
        for (TicketEntry entry:updated.getEntries()) {
            assertTrue(idArray.contains(entry.getBarCode()));
        }
    }

    @Test
    public void create() {
        assertTrue(repo.create(new ReturnTransactionImpl())>0);
    }

    @Test
    public void delete() {
        repo.delete(repo.find(35));
        assertNull(repo.find(35));
    }
}