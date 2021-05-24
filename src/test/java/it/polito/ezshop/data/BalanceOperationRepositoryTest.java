package it.polito.ezshop.data;

import org.junit.Test;

import java.io.Serializable;
import java.time.LocalDate;

import static org.junit.Assert.*;


public class BalanceOperationRepositoryTest {
    @Test
    public void find() {

        BalanceOperationRepository balanceOperationRepository = new BalanceOperationRepository();
        BalanceOperationImpl balanceOperation = new BalanceOperationImpl();
        Serializable id;


        id=balanceOperationRepository.create(balanceOperation);

        assertEquals(balanceOperation, balanceOperationRepository.find(id));

    }

    @Test
    public void findAll() {

    }

    @Test
    public void findAllBetweenDates() {

    }

    @Test
    public void update() {
 /*       BalanceOperationRepository balanceOperationRepository = new BalanceOperationRepository();
        BalanceOperation balanceOperation = new BalanceOperationImpl();
        balanceOperationRepository.create(balanceOperation);
        int id=5;
        double money=12.0;
        LocalDate date = LocalDate.of(2020,12,12);

        balanceOperation.setMoney(12.0);
        balanceOperation.setDate(date);
        balanceOperation.setType("CREDIT");
*/
        //balanceOperationRepository.update(balanceOperation);
        //BalanceOperation updated = balanceOperationRepository.find(id);
        //assertEquals(id, updated.getBalanceId());
        //assert(money==updated.getMoney());
        //assertEquals(date, balanceOperation.getDate());
        //assertEquals("CREDIT", balanceOperation.getType());



    }

    @Test
    public void create() {
        BalanceOperationRepository balanceOperationRepository = new BalanceOperationRepository();
        BalanceOperation balanceOperation = new BalanceOperationImpl();
        Integer id;

        assertTrue(balanceOperationRepository.create(balanceOperation)>0);
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