package it.polito.ezshop.data;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class BalanceOperationRepositoryTest {

    private static BalanceOperationRepository repo = new BalanceOperationRepository();
    private static List<BalanceOperation> balanceOperationList=new ArrayList<>();
    private static BalanceOperationImpl balanceOperation;
    private static Integer balanceOperationId;


    @Before
    public void init(){
        balanceOperation = new BalanceOperationImpl();
        balanceOperation.setDate(LocalDate.of(2021, 3, 9));
        balanceOperation.setMoney(12.0);
        balanceOperation.setType("CREDIT");
        balanceOperationId=repo.create(balanceOperation);
    }
    
    @Test
    public void find() {
        assert(balanceOperationId==repo.find(balanceOperationId).getBalanceId());
    }

    @Test
    public void findAll() {
        List<Integer> idArray=new ArrayList<>();
        BalanceOperationImpl balanceOperation2 = new BalanceOperationImpl();
        BalanceOperationImpl balanceOperation3 = new BalanceOperationImpl();

        balanceOperationList.add(balanceOperation2);
        balanceOperationList.add(balanceOperation3);

        idArray.add(balanceOperationId);

        for (BalanceOperation entry:balanceOperationList) {
            idArray.add((repo.create(entry)));
        }

        balanceOperationList=repo.findAll();

        for (BalanceOperation entry:balanceOperationList) {
            assertTrue(idArray.contains(entry.getBalanceId()));
        }

    }

    @Test
    public void findAllBetweenDates() {
        BalanceOperationImpl balanceOperation2 = new BalanceOperationImpl();
        BalanceOperationImpl balanceOperation3 = new BalanceOperationImpl();

        List<LocalDate> dateArray=new ArrayList<>();
        dateArray.add(LocalDate.of(2020, 1,1));
        dateArray.add(LocalDate.of(2021, 3, 9));

        balanceOperation2.setDate(LocalDate.of(2020, 1,1));
        balanceOperation3.setDate(LocalDate.of(2021, 3, 9));

        balanceOperationList.add(balanceOperation);
        balanceOperationList.add(balanceOperation2);
        balanceOperationList.add(balanceOperation3);


        for (BalanceOperation entry:balanceOperationList) {
            repo.create(entry);
        }

        balanceOperationList=repo.findAllBetweenDates(LocalDate.of(2020,9,9), LocalDate.of(2021,3,31));

        for (BalanceOperation entry:balanceOperationList) {
            assertTrue(dateArray.contains(entry.getDate()) &&
                    (entry.getDate().isBefore(LocalDate.of(2021,3,31))
                            && entry.getDate().isAfter(LocalDate.of(2020,9,9))));
        }
    }


    @Test
    public void update() {
        double newMoney=25.0;
        LocalDate newDate = LocalDate.of(2020,12,12);
        String newType = "DEBIT";

        balanceOperation.setMoney(newMoney);
        balanceOperation.setDate(newDate);
        balanceOperation.setType(newType);

        BalanceOperation updated=repo.update(balanceOperation);

        assert(newMoney==updated.getMoney());
        assertEquals(newDate, updated.getDate());
        assertEquals(newType, updated.getType());
    }

    @Test
    public void create() {
        assertTrue(balanceOperationId>0);
    }

    @Test
    public void delete() {
        repo.delete(balanceOperation);
        assertNull(repo.find(balanceOperationId));
    }

    @After
    public void stop(){

        balanceOperationList=repo.findAll();
        for (BalanceOperation u:balanceOperationList) {
            repo.delete(u);
        }
        balanceOperationList.clear();
    }
}