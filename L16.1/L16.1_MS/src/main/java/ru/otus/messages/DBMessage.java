package ru.otus.messages;

import ru.otus.MessageDto;

import java.util.List;
import java.util.UUID;

public class DBMessage extends Message {

    private final List<MessageDto> messageDtoList;

    public DBMessage(Address from, Address to, String messageId, UUID uuid, MessageDto messageDto, List<MessageDto> messageDtoList) {
        super(from, to, messageId, uuid, messageDto);
        this.messageDtoList = messageDtoList;
    }

    public List<MessageDto> getMessageDtoList() {
        return messageDtoList;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DBMessage{");
        sb.append("id=").append(getMessageId());
        sb.append("from=").append(getFrom());
        sb.append("to=").append(getTo());
        sb.append("dto=").append(getMessageDto());
        sb.append("dtoList=").append(getMessageDtoList());
        sb.append('}');
        return sb.toString();
    }

}
