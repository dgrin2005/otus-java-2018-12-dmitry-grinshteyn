package ru.otus.DataSet;

import ru.otus.Annotation.TableName;

import javax.persistence.*;

//@TableName("otus.hw10")
@Entity
@Table
public class UserDataSet extends DataSet {
    private String name;
    private int age;

    public UserDataSet() {
    }

    public UserDataSet(String name, int age) {
        super(0);
        this.name = name;
        this.age = age;
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

    @Override
    public String toString() {
        return "UserDataSet{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
