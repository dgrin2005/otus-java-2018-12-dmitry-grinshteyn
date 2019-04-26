package ru.otus.MessageSystem.Messages;

import ru.otus.DBService.DBService;
import ru.otus.DataSet.UserDataSet;
import ru.otus.Exception.MyOrmException;
import ru.otus.MessageSystem.Address;
import ru.otus.MessageSystem.Message;

import javax.servlet.http.HttpServletRequest;

public class MessageDeleteById extends MessageToDB {
    private long userId;
    private HttpServletRequest httpServletRequest;

    public MessageDeleteById(Address from, Address to, HttpServletRequest httpServletRequest, long userId) {
        super(from, to);
        this.httpServletRequest = httpServletRequest;
        this.userId = userId;
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
        message = new MessageUserList(getTo(), getTo(), httpServletRequest,
                errorMessage, "", -1, getFrom());
        dbService.getMessageSystem().sendMessage(message);
    }

}
