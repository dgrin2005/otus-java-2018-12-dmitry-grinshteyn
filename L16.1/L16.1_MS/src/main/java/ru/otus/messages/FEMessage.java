package ru.otus.messages;

import ru.otus.MessageDto;

import java.util.UUID;

public class FEMessage extends Message {

    public FEMessage(Address from, Address to, String messageId, UUID uuid, MessageDto messageDto) {
        super(from, to, messageId, uuid, messageDto);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FEMessage{");
        sb.append("id=").append(getMessageId());
        sb.append("from=").append(getFrom());
        sb.append("to=").append(getTo());
        sb.append("dto=").append(getMessageDto());
        sb.append('}');
        return sb.toString();
    }

}