package it.polito.ezshop.data;

public interface LoyaltyCard {
    public String getId();

    public void setId(String code);

    public Integer getPoints();

    public void setPoints(Integer points);

    public Customer getCustomer();

    public void setCustomer(Customer customer);
}
