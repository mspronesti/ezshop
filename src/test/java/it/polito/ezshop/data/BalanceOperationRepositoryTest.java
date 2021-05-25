package it.polito.ezshop.data;

import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class BalanceOperationRepositoryTest {
    @Test
    public void find() {

        BalanceOperationRepository balanceOperationRepository = new BalanceOperationRepository();
        BalanceOperationImpl balanceOperation = new BalanceOperationImpl();
        Integer id;
        id=balanceOperationRepository.create(balanceOperation);
        assert(id==balanceOperationRepository.find(id).getBalanceId());
        balanceOperationRepository.delete(balanceOperation);

    }

    @Test
    public void findAll() {

        BalanceOperationRepository balanceOperationRepository = new BalanceOperationRepository();
        BalanceOperationImpl balanceOperation1 = new BalanceOperationImpl();
        BalanceOperationImpl balanceOperation2 = new BalanceOperationImpl();
        BalanceOperationImpl balanceOperation3 = new BalanceOperationImpl();
        List<BalanceOperation> bOpList = new ArrayList<>();
        List<Integer> idArray=new ArrayList<>();


        bOpList.add(balanceOperation1); bOpList.add(balanceOperation2); bOpList.add(balanceOperation3);

        for (BalanceOperation entry:bOpList) {
            idArray.add((balanceOperationRepository.create(entry)));
        }

        bOpList=balanceOperationRepository.findAll();


        for (BalanceOperation entry:bOpList) {
            assertTrue(idArray.contains(entry.getBalanceId()));
        }

        for (BalanceOperation entry:bOpList) {
            balanceOperationRepository.delete(entry);
        }

    }

    @Test
    public void findAllBetweenDates() {

        BalanceOperationRepository balanceOperationRepository = new BalanceOperationRepository();
        BalanceOperationImpl balanceOperation1 = new BalanceOperationImpl();
        BalanceOperationImpl balanceOperation2 = new BalanceOperationImpl();
        BalanceOperationImpl balanceOperation3 = new BalanceOperationImpl();
        List<BalanceOperation> bOpList = new ArrayList<>();
        List<LocalDate> dateArray=new ArrayList<>();
        dateArray.add(LocalDate.of(2020, 1,1));
        dateArray.add(LocalDate.of(2021, 3, 9));

        balanceOperation1.setDate(dateArray.get(0));
        balanceOperation1.setDate(dateArray.get(1));
        balanceOperation1.setDate(LocalDate.of(2019, 2, 12));

        bOpList.add(balanceOperation1); bOpList.add(balanceOperation2); bOpList.add(balanceOperation3);

        bOpList=balanceOperationRepository.findAllBetweenDates(LocalDate.of(2019,9,9), LocalDate.of(2021,3,31));

        for (BalanceOperation entry:bOpList) {
            balanceOperationRepository.create(entry);
        }

        for (BalanceOperation entry:bOpList) {
            assertTrue(dateArray.contains(entry.getDate()));
        }

        for (BalanceOperation entry:bOpList) {
            balanceOperationRepository.delete(entry);
        }

    }


    @Test
    public void update() {
        BalanceOperationRepository balanceOperationRepository = new BalanceOperationRepository();
        BalanceOperation balanceOperation = new BalanceOperationImpl();
        Integer id;
        double money=12.0;

        id=balanceOperationRepository.create(balanceOperation);

        LocalDate date = LocalDate.of(2020,12,12);
        balanceOperation.setMoney(12.0);
        balanceOperation.setDate(date);
        balanceOperation.setType("CREDIT");

        balanceOperationRepository.update(balanceOperation);

        BalanceOperation updated = balanceOperationRepository.find(id);

        assert(money==updated.getMoney());
        assertEquals(date, updated.getDate());
        assertEquals("CREDIT", updated.getType());

        balanceOperationRepository.delete(balanceOperation);
    }

    @Test
    public void create() {
        BalanceOperationRepository balanceOperationRepository = new BalanceOperationRepository();
        BalanceOperation balanceOperation = new BalanceOperationImpl();

        assertTrue(balanceOperationRepository.create(balanceOperation)>0);

        balanceOperationRepository.delete(balanceOperation);
    }

    @Test
    public void delete() {
        BalanceOperationRepository balanceOperationRepository = new BalanceOperationRepository();
        BalanceOperation balanceOperation = new BalanceOperationImpl();
        Integer id;

        id=balanceOperationRepository.create(balanceOperation);
        balanceOperationRepository.delete(balanceOperation);
        assertNull(balanceOperationRepository.find(id));
    }

}