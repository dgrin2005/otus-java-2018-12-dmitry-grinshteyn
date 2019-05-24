package ru.otus.DataSet;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "phone_hbr")
public class PhoneDataSet extends DataSet {
    private String number;

    public PhoneDataSet() {
    }

    public PhoneDataSet(String number) {
        super(0);
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "PhoneDataSet{" +
                "id=" + getId() +
                ", number='" + number + '\'' +
                '}';
    }
}
