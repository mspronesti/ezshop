package it.polito.ezshop.data;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@DynamicInsert
public class OrderImpl implements Order {
	public enum Status{
		ISSUED,
		PAYED,
		COMPLETED
	}
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer balanceId;
    @ColumnDefault("''")
    private String status;
    private String productCode;
    @ColumnDefault("0")
    private Double pricePerUnit;
    @ColumnDefault("0")
    private Integer quantity;
    
    @Override
    public Integer getOrderId() {
        return id;
    }

    @Override
    public void setOrderId(Integer orderId) {
        this.id = orderId;
    }

    @Override
    public Integer getBalanceId() {
        return balanceId;
    }

    @Override
    public void setBalanceId(Integer balanceId) {
        this.balanceId = balanceId;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String getProductCode() {
        return productCode;
    }

    @Override
    public void setProductCode(String productCode) {
        this.productCode = productCode;
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
    public int getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
}
