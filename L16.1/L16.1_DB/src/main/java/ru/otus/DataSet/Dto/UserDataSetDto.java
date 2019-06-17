package ru.otus.dataset.dto;

import ru.otus.dataset.PhoneDataSet;
import ru.otus.dataset.UserDataSet;
import ru.otus.MessageDto;

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

    public UserDataSetDto (MessageDto messageDto) {
        this.id = messageDto.getId();
        this.name = messageDto.getName();
        this.age = messageDto.getAge();
        this.address = messageDto.getAddress();
        this.phones = messageDto.getPhones();
    }

    public static UserDataSetDto fromMessageDto (MessageDto messageDto) {
        return new UserDataSetDto(messageDto);
    }

    @Override
    public String toString() {
        return "[" +
                name +
                ", age=" + age +
                ", address=" + address +
                ", phones=" + phones +
                ']';
    }
}
