package it.polito.ezshop.data;


import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.*;


public class BalanceOperationRepositoryTest {

    private static final BalanceOperationRepository repo = new BalanceOperationRepository();

    @Test
    public void find() {
        int balanceOperationId=55;
        assertEquals(balanceOperationId,repo.find(balanceOperationId).getBalanceId());
    }

    @Test
    public void findAll() {
        assertEquals(repo.findAll().getClass(), ArrayList.class);
        assertEquals(repo.findAll().get(0).getClass(), BalanceOperationImpl.class);
    }

    @Test
    public void findAllBetweenDates() {
        LocalDate from =LocalDate.of(2020,9,9);
        LocalDate to =LocalDate.of(2021,5,31);
        for (BalanceOperation entry:
                repo.findAllBetweenDates(from,to)) {
            assertTrue(entry.getDate().isBefore(to)
                            && entry.getDate().isAfter(from));
        }
        assertEquals(repo.findAllBetweenDates(from, to).getClass(), ArrayList.class);
        assertEquals(repo.findAllBetweenDates(from, to).get(0).getClass(), BalanceOperationImpl.class);
    }


    @Test
    public void update() {
        double newMoney=25.0;
        LocalDate newDate = LocalDate.of(2020,12,12);
        String newType = "CREDIT";
        BalanceOperation balanceOperation = repo.find(55);

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
        assertTrue(repo.create(new BalanceOperationImpl())>0);
    }

    @Test
    public void delete() {
        repo.delete(repo.find(53));
        assertNull(repo.find(53));
    }
}