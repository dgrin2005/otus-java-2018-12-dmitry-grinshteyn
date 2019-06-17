package ru.otus.dataset;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "address_hbr")
public class AddressDataSet extends DataSet {
    private String street;

    public AddressDataSet() {
        this.street = "";
    }

    public AddressDataSet(String street) {
        super(0);
        this.street = street;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String toString() {
        return "AddressDataSet{" +
                "id=" + getId() +
                ", street='" + street + '\'' +
                '}';
    }
}
