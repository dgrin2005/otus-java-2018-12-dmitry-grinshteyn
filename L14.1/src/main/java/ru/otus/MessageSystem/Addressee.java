package ru.otus.MessageSystem;

public interface Addressee {

    void initInMessageSystem();

    Address getAddress();

    MessageSystem getMessageSystem();
}
