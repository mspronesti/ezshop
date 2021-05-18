package it.polito.ezshop.data;

import jakarta.validation.constraints.Size;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Properties;

@Entity
@DynamicInsert
public class LoyaltyCardImpl implements LoyaltyCard {
    public static final String PATTERN = "^\\d{10}$";

    @Id
    @GeneratedValue(generator = "cardCode")
    @GenericGenerator(name = "cardCode", strategy = "it.polito.ezshop.util.LoyaltyCardIdGenerator")
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
