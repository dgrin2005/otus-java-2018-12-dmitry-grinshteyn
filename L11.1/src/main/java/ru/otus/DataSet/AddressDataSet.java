package ru.otus.DataSet;

import javax.persistence.*;

@Entity
@Table(name = "address")
public class AddressDataSet extends DataSet {
    private String street;

    public AddressDataSet() {
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
                "street='" + street + '\'' +
                '}';
    }
}
