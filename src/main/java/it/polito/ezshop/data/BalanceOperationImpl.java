package it.polito.ezshop.data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class BalanceOperationImpl implements BalanceOperation {
    public enum Type {
        CREDIT,
        DEBIT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDate date;
    private Double money = 0d;
    private String type = "";

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
    
    @Override
    public boolean equals(Object o) {
    	if(o == this) 
    		return true;
    	if(!(o instanceof BalanceOperation))
    		return false;
    	
    	BalanceOperation other = (BalanceOperationImpl)o;
    	
    	return this.date.equals(other.getDate()) && this.id == other.getBalanceId()
    			 && this.money == other.getMoney() && this.type.equals(other.getType());
    }
}
