package it.polito.ezshop.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ProductImpl implements Product {
  public static final String RFIDPATTERN = "^\\d{10}$";
  public enum Status {
    AVAILABLE,
    SELLING,
    SOLD
  }

  @Id
  @Column(unique = true)
  private String id;
  @ManyToOne
  private ProductTypeImpl productType;
  private Status status = Status.AVAILABLE;

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

  @Override
  public Status getStatus() {
    return status;
  }

  @Override
  public void setStatus(Status status) {
    this.status = status;
  }
}
