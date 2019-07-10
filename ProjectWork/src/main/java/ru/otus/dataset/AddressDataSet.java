package ru.otus.dataset;

import org.bson.types.ObjectId;
import ru.otus.annotation.Document;

@Document("ads")
public class AddressDataSet extends DataSet {
    private String street;

    public AddressDataSet() {
    }

    public AddressDataSet(String street) {
        super(new ObjectId());
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
