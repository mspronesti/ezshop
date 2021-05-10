package it.polito.ezshop.data;

import javax.persistence.*;

@Entity
public class CustomerImpl implements Customer {

    @Embeddable
    private static class LoyaltyCard {
        String code;
        Integer points;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    @Embedded
    private final LoyaltyCard loyaltyCard = new LoyaltyCard();

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getCustomerName() {
        return name;
    }

    @Override
    public void setCustomerName(String customerName) {
        this.name = customerName;
    }

    @Override
    public String getCustomerCard() {
        return this.loyaltyCard.code;
    }

    @Override
    public void setCustomerCard(String customerCard) {
        this.loyaltyCard.code = customerCard;
    }

    @Override
    public Integer getPoints() {
        return this.loyaltyCard.points;
    }

    @Override
    public void setPoints(Integer points) {
        this.loyaltyCard.points = points;
    }
}