package ru.otus.messages;

import ru.otus.MessageDto;

import java.util.UUID;

public abstract class Message {

    public final static String MESSAGE_ID_CREATE_NEW_USER = "CreateNewUser";
    public final static String MESSAGE_ID_DELETE_USER = "DeleteUser";
    public final static String MESSAGE_ID_FIND_USER = "FindUser";
    public final static String MESSAGE_ID_USER_LIST = "UserList";
    public final static String MESSAGE_ID_SHOW_PAGE = "ShowPage";

    private final Address from;
    private final Address to;
    private final String messageId;
    private final UUID uuid;
    private final MessageDto messageDto;

    public Message(Address from, Address to, String messageId, UUID uuid, MessageDto messageDto) {
        this.from = from;
        this.to = to;
        this.messageId = messageId;
        this.uuid = uuid;
        this.messageDto = messageDto;
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

    public MessageDto getMessageDto() {
        return messageDto;
    }

    public UUID getUuid() {
        return uuid;
    }

}
