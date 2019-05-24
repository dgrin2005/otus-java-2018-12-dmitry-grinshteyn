package ru.otus.MessageSystem;

import ru.otus.Exception.MyOrmException;

import java.io.IOException;
import java.util.UUID;

public abstract class Message {
    private final Address from;
    private final Address to;
    private final String messageId;

    public Message(Address from, Address to, String messageId) {
        this.from = from;
        this.to = to;
        this.messageId = messageId;
    }

    public Address getFrom() {
        return from;
    }

    public Address getTo() {
        return to;
    }

    public String getMessageId() {
        return messageId;
    }

    public abstract void exec(Addressee addressee) throws MyOrmException, IOException;
}
