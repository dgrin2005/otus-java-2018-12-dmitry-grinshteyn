package ru.otus.dataset;

import org.bson.types.ObjectId;
import ru.otus.annotation.Document;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddressDataSet)) return false;
        if (!super.equals(o)) return false;
        AddressDataSet that = (AddressDataSet) o;
        return Objects.equals(getStreet(), that.getStreet());
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), getStreet());
    }
}
