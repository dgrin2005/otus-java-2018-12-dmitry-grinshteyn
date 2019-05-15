package ru.otus.MessageSystem.Messages.DB;

import ru.otus.DBService.DBService;
import ru.otus.DataSet.UserDataSet;
import ru.otus.MessageSystem.Address;
import ru.otus.MessageSystem.Message;
import ru.otus.MessageSystem.Messages.WS.MessageSetErrorMessage;
import ru.otus.MessageSystem.Messages.WS.MessageSetUserFoundedById;

import java.util.UUID;

import static ru.otus.WebServer.WebServerUtilites.userDataSetToDto;

public class MessageFindUser extends MessageToDB {

    private long userId;
    private final UUID uuid;

    public MessageFindUser(Address from, Address to, long userId, UUID uuid) {
        super(from, to);
        this.userId = userId;
        this.uuid = uuid;
    }

    @Override
    public void exec(DBService dbService) {
        String errorMessage = "";
        String userFoundedById = "";
        try {
            userFoundedById = userDataSetToDto(dbService.getById(userId, UserDataSet.class));
        } catch (Exception e) {
            errorMessage = e.getMessage();
            userId = -1;
        }
        Message message = new MessageSetUserFoundedById(getTo(), getFrom(), userFoundedById);
        dbService.getMessageSystem().sendMessage(message);
        message = new MessageSetErrorMessage(getTo(), getFrom(), errorMessage);
        dbService.getMessageSystem().sendMessage(message);
        message = new MessageUserList(getTo(), getTo(),
                errorMessage, userFoundedById, userId, getFrom(), uuid);
        dbService.getMessageSystem().sendMessage(message);
    }
}
