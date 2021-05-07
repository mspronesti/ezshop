package it.polito.ezshop.data;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDate;

public class BalanceOperationImpl implements BalanceOperation {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Integer id;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDate date;
    private Double money;
    private String type;

    @Override
    public int getBalanceId() {
        return 0;
    }

    @Override
    public void setBalanceId(int balanceId) {

    }

    @Override
    public LocalDate getDate() {
        return null;
    }

    @Override
    public void setDate(LocalDate date) {

    }

    @Override
    public double getMoney() {
        return 0;
    }

    @Override
    public void setMoney(double money) {

    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public void setType(String type) {

    }
}
