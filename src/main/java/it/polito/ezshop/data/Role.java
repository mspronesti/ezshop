package it.polito.ezshop.data;

public enum Role {
    Administrator,
    ShopManager,
    Cashier;

    public static final String pattern = "Administrator|ShopManager|Cashier";
}
