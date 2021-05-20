package it.polito.ezshop.data;

public enum Role {
    Administrator,
    ShopManager,
    Cashier;

    public static final String PATTERN = "Administrator|ShopManager|Cashier";
}
