package ru.otus.DataSet;

import ru.otus.Annotation.MyOrmTable;

import javax.persistence.*;

@Entity
@Table(name = "address_hbr")
@MyOrmTable("address_my_orm")
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
                "id=" + getId() +
                ", street='" + street + '\'' +
                '}';
    }
}
