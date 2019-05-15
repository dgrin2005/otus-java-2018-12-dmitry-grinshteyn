package ru.otus.MessageSystem.Messages.DB;

import ru.otus.DBService.DBService;
import ru.otus.DataSet.UserDataSet;
import ru.otus.Exception.MyOrmException;
import ru.otus.MessageSystem.Address;
import ru.otus.MessageSystem.Message;
import ru.otus.MessageSystem.Messages.WS.MessageSetErrorMessage;

import java.util.UUID;

public class MessageDeleteById extends MessageToDB {
    private final long userId;
    private final UUID uuid;

    public MessageDeleteById(Address from, Address to, long userId, UUID uuid) {
        super(from, to);
        this.userId = userId;
        this.uuid = uuid;
    }

    @Override
    public void exec(DBService dbService) {
        String errorMessage = "";
        try {
            dbService.deleteById(userId, UserDataSet.class);
        } catch (MyOrmException e) {
            errorMessage = e.getMessage();
        }
        Message message = new MessageSetErrorMessage(getTo(), getFrom(), errorMessage);
        dbService.getMessageSystem().sendMessage(message);
        message = new MessageUserList(getTo(), getTo(),
                errorMessage, "", -1, getFrom(), uuid);
        dbService.getMessageSystem().sendMessage(message);
    }

}
