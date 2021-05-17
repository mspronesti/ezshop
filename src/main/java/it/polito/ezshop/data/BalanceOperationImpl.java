package it.polito.ezshop.data;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@DynamicInsert
public class BalanceOperationImpl implements BalanceOperation {
    public enum Type {
        CREDIT,
        DEBIT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private LocalDate date;
    @ColumnDefault("0")
    private Double money;
    @ColumnDefault("''")
    private String type;

    @Override
    public int getBalanceId() {
        return this.id;
    }

    @Override
    public void setBalanceId(int balanceId) {
        this.id = balanceId;
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public double getMoney() {
        return money;
    }

    @Override
    public void setMoney(double money) {
        this.money = money;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Transient
    public void setType(Type type) {
        this.type = type.name();
    }
}
