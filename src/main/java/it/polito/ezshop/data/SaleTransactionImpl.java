package it.polito.ezshop.data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class SaleTransactionImpl implements SaleTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ElementCollection
    private List<TicketEntry> entries;
    private Double discountRate;
    private Double price;

    @Override
    public Integer getTicketNumber() {
        return id;
    }

    @Override
    public void setTicketNumber(Integer ticketNumber) {
        this.id = ticketNumber;
    }

    @Override
    public List<TicketEntry> getEntries() {
        return entries;
    }

    @Override
    public void setEntries(List<TicketEntry> entries) {
        this.entries = entries;
    }

    @Override
    public double getDiscountRate() {
        return discountRate;
    }

    @Override
    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public void setPrice(double price) {
        this.price = price;
    }
}
