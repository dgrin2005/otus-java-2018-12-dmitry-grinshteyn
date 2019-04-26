package ru.otus.MessageSystem.Messages.DB;

import ru.otus.DBService.DBService;
import ru.otus.DataSet.UserDataSet;
import ru.otus.Exception.MyOrmException;
import ru.otus.MessageSystem.Address;
import ru.otus.MessageSystem.Message;
import ru.otus.MessageSystem.Messages.WS.MessageSetErrorMessage;

public class MessageCreateNewUser extends MessageToDB {
    private final UserDataSet userDataSet;

    public MessageCreateNewUser(Address from, Address to, UserDataSet userDataSet) {
        super(from, to);
        this.userDataSet = userDataSet;
    }

    @Override
    public void exec(DBService dbService) {
        String errorMessage = "";
        try {
            dbService.create(userDataSet);
        } catch (MyOrmException e) {
            errorMessage = e.getMessage();
        }
        Message message = new MessageSetErrorMessage(getTo(), getFrom(), errorMessage);
        dbService.getMessageSystem().sendMessage(message);
    }

}
