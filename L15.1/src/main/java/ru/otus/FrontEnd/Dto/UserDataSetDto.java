package ru.otus.FrontEnd.Dto;

import ru.otus.DataSet.PhoneDataSet;
import ru.otus.DataSet.UserDataSet;

import java.util.stream.Collectors;

public class UserDataSetDto {
    private long id;
    private String name;
    private int age;
    private String address;
    private String phones;

    public UserDataSetDto() {
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public String getPhones() {
        return phones;
    }

    public UserDataSetDto (UserDataSet userDataSet) {
        this.id = userDataSet.getId();
        this.name = userDataSet.getName();
        this.age = userDataSet.getAge();
        this.address = userDataSet.getAddress().getStreet();
        this.phones = userDataSet.getPhones().stream()
                .map(PhoneDataSet::getNumber)
                .collect(Collectors.joining(", "));
    }

    @Override
    public String toString() {
        return "[" +
                name +
                ", возраст " + age +
                ", адрес " + address +
                ", телефоны " + phones +
                ']';
    }
}
