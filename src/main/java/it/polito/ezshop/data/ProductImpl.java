package it.polito.ezshop.data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ProductImpl implements Product {
  public static final String RFIDPATTERN = "^\\d{10}$";
  @Id
  private String id;
  @ManyToOne
  private ProductTypeImpl productType;

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void setId(String id) {
    this.id = id;
  }

  @Override
  public ProductType getProductType() {
    return productType;
  }

  @Override
  public void setProductType(ProductTypeImpl productType) {
    this.productType = productType;
  }
}
