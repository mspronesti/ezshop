package it.polito.ezshop.data;

import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ReturnTransactionRepositoryTest {

    private static ReturnTransactionRepository repo = new ReturnTransactionRepository();
    private static BalanceOperationRepository balanceRepo = new BalanceOperationRepository();
    private static SaleTransactionRepository saleRepo = new SaleTransactionRepository();
    private static List<ReturnTransactionImpl> returnTransactionList=new ArrayList<>();
    private static ReturnTransactionImpl returnTransaction = new ReturnTransactionImpl();
    private static Integer returnTransactionId;



    @Before
    public void init(){
        returnTransaction.setPrice(12.0);
        returnTransaction.setDiscountRate(1.0);

        BalanceOperationImpl balance = new BalanceOperationImpl();
        int balanceId=balanceRepo.create(balance);
        balance.setBalanceId(balanceId);
        returnTransaction.setPayment(balance);

        SaleTransactionImpl sale = new SaleTransactionImpl();
        int saleId=saleRepo.create(sale);
        sale.setTicketNumber(saleId);
        returnTransaction.setSaleTransaction(sale);

        returnTransaction.setEntries(new ArrayList<TicketEntry>());
        returnTransactionId=repo.create(returnTransaction);
    }
    
    @Test
    public void find() {
        assertEquals(returnTransactionId,repo.find(returnTransactionId).getTicketNumber());
    }

    @Test
    public void findAll() {
        List<Integer> idArray = new ArrayList<>();

        ReturnTransactionImpl returnTransaction2 = new ReturnTransactionImpl();
        ReturnTransactionImpl returnTransaction3 = new ReturnTransactionImpl();

        returnTransactionList.add(returnTransaction2);
        returnTransactionList.add(returnTransaction3);

        idArray.add(returnTransactionId);

        for (ReturnTransactionImpl entry:returnTransactionList) {
            idArray.add((repo.create(entry)));
        }

        returnTransactionList=repo.findAll();


        for (ReturnTransactionImpl entry:returnTransactionList) {
            assertTrue(idArray.contains(entry.getTicketNumber()));
        }

    }

    @Test
    public void create() {
        assertTrue(returnTransactionId>0);
    }

    @Test
    public void update() {
        double newPrice = 15.0;
        double newDiscount = 2.0;
        BalanceOperationImpl newBalOp = new BalanceOperationImpl();
        SaleTransactionImpl newSale = new SaleTransactionImpl();
        List<TicketEntry> newEntries = new ArrayList<>();

        returnTransaction.setPrice(newPrice);
        returnTransaction.setDiscountRate(newDiscount);
        int bId=balanceRepo.create(newBalOp);
        newBalOp.setBalanceId(bId);
        returnTransaction.setPayment(newBalOp);
        int sId=saleRepo.create(newSale);
        newSale.setTicketNumber(sId);
        returnTransaction.setSaleTransaction(newSale);
        returnTransaction.setEntries(newEntries);

        repo.update(returnTransaction);

        ReturnTransactionImpl updated=repo.find(returnTransactionId);

        assert(newPrice==updated.getPrice());
        assert(newDiscount==updated.getDiscountRate());
        assertEquals(newBalOp.getBalanceId(), updated.getPayment().getBalanceId());
        assertEquals(newSale.getTicketNumber(), updated.getSaleTransaction().getTicketNumber());
        assertEquals(newEntries, updated.getEntries());
    }

    @Test
    public void delete() {
        repo.delete(returnTransaction);
        assertNull(repo.find(returnTransactionId));
    }

    @After
    public void stop(){
        returnTransactionList=repo.findAll();
        for (ReturnTransactionImpl u:returnTransactionList) {
            repo.delete(u);
        }
        for (SaleTransaction s:saleRepo.findAll()) {
            saleRepo.delete(s);
        }
        for (BalanceOperation b:balanceRepo.findAll()) {
            balanceRepo.delete(b);
        }
    }
}