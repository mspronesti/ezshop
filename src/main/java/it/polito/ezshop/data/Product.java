package it.polito.ezshop.data;

public interface Product {
    String getId();

    void setId(String id);

    ProductType getProductType();

    void setProductType(ProductTypeImpl productType);

    ProductImpl.Status getStatus();

    void setStatus(ProductImpl.Status status);
}
