package it.polito.ezshop.data;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@DynamicInsert
public class CustomerImpl implements Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ColumnDefault("''")
    private String name;
    @OneToOne
    public LoyaltyCardImpl loyaltyCard;
    @Transient
    private final LoyaltyCardRepository loyaltyCardRepository = new LoyaltyCardRepository();

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
        return loyaltyCard != null ? loyaltyCard.getId() : "";
    }

    @Override
    public void setCustomerCard(String customerCard) {
        LoyaltyCard card = loyaltyCardRepository.find(customerCard);
        if (card != null && card.getCustomer() == null) {
            loyaltyCard = (LoyaltyCardImpl) card;
        }
    }

    @Override
    public Integer getPoints() {
        return loyaltyCard != null ? loyaltyCard.getPoints() : null;
    }

    @Override
    public void setPoints(Integer points) {
        loyaltyCard.setPoints(points);
    }
}
