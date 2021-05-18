package it.polito.ezshop.data;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Embeddable
@DynamicInsert
public class TicketEntryImpl implements TicketEntry {
    private String barcode = "";
    private String productDescription = "";
    private Integer amount = 0;
    private Double pricePerUnit = 0d;
    private Double discountRate = 0d;

    @Override
    public String getBarCode() {
        return barcode;
    }

    @Override
    public void setBarCode(String barCode) {
        this.barcode = barCode;
    }

    @Override
    public String getProductDescription() {
        return productDescription;
    }

    @Override
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public double getPricePerUnit() {
        return pricePerUnit;
    }

    @Override
    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    @Override
    public double getDiscountRate() {
        return discountRate;
    }

    @Override
    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }
}
