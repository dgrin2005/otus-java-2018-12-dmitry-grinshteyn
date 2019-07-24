package ru.otus.dataset;

import org.bson.types.ObjectId;
import ru.otus.annotation.Field;
import ru.otus.annotation.Document;

import java.util.List;
import java.util.Objects;

@Document("uds")
public class UserDataSet extends DataSet {
    @Field("FIO")
    private String name;
    private int age;
    private AddressDataSet address;
    private List<PhoneDataSet> phones;

    public UserDataSet() {
    }

    public UserDataSet(String name, int age) {
        super(new ObjectId());
        this.name = name;
        this.age = age;
    }

    public UserDataSet(String name, int age, AddressDataSet address, List<PhoneDataSet> phones) {
        super(new ObjectId());
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDataSet)) return false;
        if (!super.equals(o)) return false;
        UserDataSet that = (UserDataSet) o;
        return getAge() == that.getAge() &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getAddress(), that.getAddress()) &&
                Objects.equals(getPhones(), that.getPhones());
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), getName(), getAge(), getAddress(), getPhones());
    }
}
