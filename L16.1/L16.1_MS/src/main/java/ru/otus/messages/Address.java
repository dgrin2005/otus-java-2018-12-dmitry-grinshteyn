package ru.otus.messages;

public class Address {

    private final String className;
    private final int id;

    public Address(String className, int id) {
        this.className = className;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        return className != null ? (className.equals(address.className) && id == address.id) : address.className == null;
    }

    @Override
    public int hashCode() {
        return className != null ? className.hashCode() : 0;
    }

    public String getClassName() {
        return className;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Address{" +
                "className='" + className + '\'' +
                "id='" + id + '\'' +
                '}';
    }
}
