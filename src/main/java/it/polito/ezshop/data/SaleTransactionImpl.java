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
    private List<TicketEntryImpl> entries;
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
    @SuppressWarnings("unchecked")
    public List<TicketEntry> getEntries() {
        return (List<TicketEntry>)(Object)entries;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setEntries(List<TicketEntry> entries) {
        this.entries = (List<TicketEntryImpl>)(Object)entries;
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