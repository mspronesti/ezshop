package it.polito.ezshop.data;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.List;

@Entity
public class SaleTransactionImpl implements SaleTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ElementCollection
    private List<TicketEntryImpl> entries;
    @ColumnDefault("0")
    private Double discountRate;
    @ColumnDefault("0")
    private Double price;
    @OneToOne
    private BalanceOperationImpl payment;

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

    public BalanceOperationImpl getPayment() {
        return payment;
    }

    public void setPayment(BalanceOperationImpl payment) {
        this.payment = payment;
    }

    public TicketEntry getEntryByBarcode(String barcode) {
        return entries
                .stream()
                .filter(e -> e.getBarCode().equals(barcode))
                .findFirst()
                .orElse(null);
    }

    public void updatePrice() {
        price = entries
                .stream()
                .mapToDouble(e -> e.getAmount() * (1d - e.getDiscountRate()) * e.getPricePerUnit())
                .sum() * (1d - discountRate);
    }
}
