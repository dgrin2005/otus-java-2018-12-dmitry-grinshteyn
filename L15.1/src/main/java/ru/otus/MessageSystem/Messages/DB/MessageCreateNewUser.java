package ru.otus.MessageSystem.Messages.DB;

import ru.otus.DBService.DBService;
import ru.otus.DataSet.UserDataSet;
import ru.otus.Exception.MyOrmException;
import ru.otus.MessageSystem.Address;
import ru.otus.MessageSystem.Message;

import java.util.UUID;

public class MessageCreateNewUser extends MessageToDB {
    private final UserDataSet userDataSet;
    private final UUID uuid;

    public MessageCreateNewUser(Address from, Address to, UserDataSet userDataSet, UUID uuid) {
        super(from, to);
        this.userDataSet = userDataSet;
        this.uuid = uuid;
    }

    @Override
    public void exec(DBService dbService) {
        String errorMessage = "";
        try {
            dbService.create(userDataSet);
        } catch (MyOrmException e) {
            errorMessage = e.getMessage();
        }
        Message message = new MessageUserList(getTo(), getTo(),
                getFrom(), errorMessage, uuid);
        dbService.getMessageSystem().sendMessage(message);
    }

}
