package ru.otus.DataSet;

import ru.otus.DataSet.Dto.UserDataSetDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_hbr")
public class UserDataSet extends DataSet {
    private String name;
    private int age;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    private AddressDataSet address;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private List<PhoneDataSet> phones;

    public UserDataSet() {
    }

    public UserDataSet(String name, int age) {
        super(0);
        this.name = name;
        this.age = age;
        this.address = new AddressDataSet();
        this.phones = new ArrayList<>();
    }

    public UserDataSet(String name, int age, AddressDataSet address, List<PhoneDataSet> phones) {
        super(0);
        this.name = name;
        this.age = age;
        this.address = address;
        this.phones = phones;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public AddressDataSet getAddress() {
        return address;
    }

    public void setAddress(AddressDataSet address) {
        this.address = address;
    }

    public List<PhoneDataSet> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneDataSet> phones) {
        this.phones = phones;
    }

    @Override
    public String toString() {
        return "\nUserDataSet{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", address=" + address +
                ", phones=" + phones +
                '}';
    }

    public UserDataSetDto from() {
        return new UserDataSetDto(this);
    }

}
