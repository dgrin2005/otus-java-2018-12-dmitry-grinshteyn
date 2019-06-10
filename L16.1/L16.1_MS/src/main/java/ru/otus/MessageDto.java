package ru.otus;

public class MessageDto {
    private long id;
    private String name;
    private int age;
    private String address;
    private String phones;
    private long userId;
    private String userFoundedById;
    private String errorMessage;

    public MessageDto() {
    }

    public MessageDto(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public MessageDto(long userId, String userFoundedById, String errorMessage) {
        this.userId = userId;
        this.userFoundedById = userFoundedById;
        this.errorMessage = errorMessage;
    }

    public MessageDto(long id, String name, int age, String address, String phones, String errorMessage) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.address = address;
        this.phones = phones;
        this.errorMessage = errorMessage;
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

    public long getUserId() {
        return userId;
    }

    public String getUserFoundedById() {
        return userFoundedById;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return "[" +
                name +
                ", возраст " + age +
                ", адрес " + address +
                ", телефоны " + phones +
                ", userId " + userId +
                ", userFoundedById " + userFoundedById +
                ']';
    }
}
