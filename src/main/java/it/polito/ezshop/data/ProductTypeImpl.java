package it.polito.ezshop.data;

import javax.persistence.*;

@Entity
public class ProductTypeImpl implements ProductType {

    @Embeddable
    private static class Position {
        private static final String SEPARATOR = "-";
        String aisleID;
        String rackID;
        String levelID;

        public String toString() {
            return aisleID + SEPARATOR + rackID + SEPARATOR + levelID;
        }

        public Position() {}
        Position(String position) {
            String[] parts = position.split(SEPARATOR);
            aisleID = parts[0];
            rackID = parts[1];
            levelID = parts[2];
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer quantity;
    private String note;
    private String description;
    private String barcode;
    private Double pricePerUnit;
    @Embedded
    private Position position = new Position();

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String getLocation() {
        return position.toString();
    }

    @Override
    public void setLocation(String location) {
        position = new Position(location);
    }

    @Override
    public String getNote() {
        return note;
    }

    @Override
    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String getProductDescription() {
        return description;
    }

    @Override
    public void setProductDescription(String productDescription) {
        this.description = productDescription;
    }

    @Override
    public String getBarCode() {
        return barcode;
    }

    @Override
    public void setBarCode(String barCode) {
        this.barcode = barCode;
    }

    @Override
    public Double getPricePerUnit() {
        return pricePerUnit;
    }

    @Override
    public void setPricePerUnit(Double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }
}
