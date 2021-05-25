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
        SaleTransactionRepository saleTransactionRepository = new SaleTransactionRepository();
        List<SaleTransaction> saleTransactionList = new ArrayList<>();
        List<Integer> idArray = new ArrayList<>();
        
        SaleTransactionImpl tr1 = new SaleTransactionImpl();
        SaleTransactionImpl tr2 = new SaleTransactionImpl();
        SaleTransactionImpl tr3 = new SaleTransactionImpl();
        
        saleTransactionList.add(tr1);
        saleTransactionList.add(tr2);
        saleTransactionList.add(tr3);

        for (SaleTransaction entry:saleTransactionList){
            idArray.add(saleTransactionRepository.create(entry));
        }

        for (SaleTransaction entry:saleTransactionList) {
            assertTrue(idArray.contains(entry.getTicketNumber()));
        }

        for (SaleTransaction entry:saleTransactionList){
            saleTransactionRepository.delete(entry);
        }
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
        SaleTransactionImpl saleTransaction = new SaleTransactionImpl();
        SaleTransactionRepository saleTransactionRepository = new SaleTransactionRepository();
        BalanceOperationImpl balanceOp = new BalanceOperationImpl();
        BalanceOperationRepository balanceOperationRepository = new BalanceOperationRepository();

        List<TicketEntry> ticketList = new ArrayList<>();
        List<String> idArray=new ArrayList<>();


        TicketEntryImpl tk1 = new TicketEntryImpl();
        TicketEntryImpl tk2 = new TicketEntryImpl();
        String barcode1="313151";
        String barcode2="313191";
        tk1.setBarCode(barcode1);
        tk2.setBarCode(barcode2);
        ticketList.add(tk1);
        ticketList.add(tk2);

        for (TicketEntry entry:ticketList) {
            idArray.add(entry.getBarCode());
        }

        double price = 50;
        double discountRate = 12.50;

        Integer id = saleTransactionRepository.create(saleTransaction);
        saleTransaction.setDiscountRate(discountRate);
        saleTransaction.setPrice(price);
        saleTransaction.setEntries(ticketList);
        saleTransaction.setPayment(balanceOp);

        int balanceId = balanceOperationRepository.create(balanceOp);


        saleTransaction.setEntries(ticketList);

        SaleTransaction saleTransaction2 = saleTransactionRepository.update(saleTransaction);

        assertEquals(id,saleTransactionRepository.find(id).getTicketNumber());
        assertEquals(balanceId, saleTransaction.getPayment().getBalanceId());
        assert(price == saleTransaction2.getPrice());
        assert(discountRate == saleTransaction2.getDiscountRate());

        for (TicketEntry entry:ticketList) {
            assertTrue(idArray.contains(entry.getBarCode()));
        }

        balanceOperationRepository.delete(balanceOp);
        saleTransactionRepository.delete(saleTransaction);

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