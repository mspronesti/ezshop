package it.polito.ezshop.data;

import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@DynamicInsert
public class LoyaltyCardImpl implements LoyaltyCard {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Size(max = 10)
    private String id;
    @ColumnDefault("0")
    private Integer points;
    @OneToOne(mappedBy = "loyaltyCard", cascade = CascadeType.ALL)
    private CustomerImpl customer;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Integer getPoints() {
        return points;
    }

    @Override
    public void setPoints(Integer points) {
        this.points = points;
    }

    @Override
    public Customer getCustomer() {
        return customer;
    }

    @Override
    public void setCustomer(Customer customer) {
        this.customer = (CustomerImpl) customer;
    }
}
