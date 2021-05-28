package it.polito.ezshop.data;

import javax.persistence.*;

@Entity
public class CustomerImpl implements Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
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
        if (customerCard.isEmpty()) {
        	loyaltyCard = null;
        }
        LoyaltyCard card = loyaltyCardRepository.find(customerCard);
        if (card != null && (card.getCustomer() == null || card.getCustomer().getId().equals(this.id))) {
            loyaltyCard = (LoyaltyCardImpl) card;
        }
    }

    @Override
    public Integer getPoints() {
        return loyaltyCard != null ? loyaltyCard.getPoints() : null;
    }

    @Override
    public void setPoints(Integer points) {
        if (loyaltyCard != null) {
            loyaltyCard.setPoints(points);
        }
    }
}
