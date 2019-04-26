package ru.otus.MessageSystem.Messages;

import ru.otus.DBService.DBService;
import ru.otus.DataSet.UserDataSet;
import ru.otus.MessageSystem.Address;
import ru.otus.MessageSystem.Message;

import javax.servlet.http.HttpServletRequest;

import static ru.otus.WebServer.WebServerUtilites.userDataSetToDto;

public class MessageFindUser extends MessageToDB {

    private long userId;
    private final HttpServletRequest httpServletRequest;

    public MessageFindUser(Address from, Address to, HttpServletRequest httpServletRequest, long userId) {
        super(from, to);
        this.httpServletRequest = httpServletRequest;
        this.userId = userId;
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
        message = new MessageUserList(getTo(), getTo(), httpServletRequest,
                errorMessage, userFoundedById, userId, getFrom());
        dbService.getMessageSystem().sendMessage(message);
    }
}
