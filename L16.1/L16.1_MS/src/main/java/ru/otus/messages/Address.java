package ru.otus.messages;

public class Address {

    private final String className;

    public Address(String className) {
        this.className = className;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        return className != null ? (className.equals(address.className)) : address.className == null;
    }

    @Override
    public int hashCode() {
        return className != null ? className.hashCode() : 0;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public String toString() {
        return "Address{" +
                "className='" + className + '\'' +
                '}';
    }
}
