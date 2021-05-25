package it.polito.ezshop.data;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ReturnTransactionRepositoryTest {

    private static ReturnTransactionRepository repo = new ReturnTransactionRepository();
    private static List<ReturnTransactionImpl> returnTransactionList=new ArrayList<>();
    private static ReturnTransactionImpl returnTransaction = new ReturnTransactionImpl();
    private static Integer returnTransactionId;



    @BeforeClass
    static public void init(){
        returnTransaction.setPrice(12.0);
        returnTransaction.setDiscountRate(1.0);
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
    }

    @Test
    public void delete() {
        repo.delete(returnTransaction);
        assertNull(repo.find(returnTransactionId));
    }

    @AfterClass
    static public void stop(){
        if((returnTransactionList=repo.findAll())!=null)
            for (ReturnTransactionImpl u:returnTransactionList) {
                repo.delete(u);
            }
    }
}