package ru.otus.FrontEndService;

import ru.otus.DataSet.UserDataSet;
import ru.otus.MessageSystem.*;
import ru.otus.MessageSystem.Messages.DB.*;
import ru.otus.WebServer.Dto.UserDataSetDto;
import ru.otus.WebServer.UserDataSetServlet;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FrontEndService implements Addressee {

    private final static Logger logger = Logger.getLogger(FrontEndService.class.getName());

    public final static String MESSAGE_ID_CREATE_NEW_USER = "CreateNewUser";
    public final static String MESSAGE_ID_DELETE_USER = "DeleteUser";
    public final static String MESSAGE_ID_FIND_USER = "FindUser";
    public final static String MESSAGE_ID_USER_LIST = "UserList";

    private final MessageSystemContext messageSystemContext;
    private final Address address;

    private final LinkedBlockingQueue<Integer> linkedBlockingQueue;

    public FrontEndService(MessageSystemContext messageSystemContext, Address address) {
        this.messageSystemContext = messageSystemContext;
        this.address = address;
        this.messageSystemContext.getMessageSystem().addAddressee(this);
        this.linkedBlockingQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public MessageSystem getMessageSystem() {
        return messageSystemContext.getMessageSystem();
    }

    public void sendMessage(String messageId, UserDataSet userDataSet,
                            UserDataSetServlet userDataSetServlet) {
        if (messageId.equals(MESSAGE_ID_CREATE_NEW_USER)) {
            Message message = new MessageCreateNewUser(address, messageSystemContext.getDbAddress(), userDataSet, userDataSetServlet);
            messageSystemContext.getMessageSystem().sendMessage(message);
        }
    }

    public void sendMessage(String messageId,
                            String errorMessage, String userFoundedById, long userId,
                            UserDataSetServlet userDataSetServlet) {
        if (messageId.equals(MESSAGE_ID_DELETE_USER)) {
            Message message = new MessageDeleteById(address, messageSystemContext.getDbAddress(), userId, userDataSetServlet);
            messageSystemContext.getMessageSystem().sendMessage(message);
        }
        if (messageId.equals(MESSAGE_ID_FIND_USER)) {
            Message message = new MessageFindUser(address, messageSystemContext.getDbAddress(), userId, userDataSetServlet);
            messageSystemContext.getMessageSystem().sendMessage(message);
        }
        if (messageId.equals(MESSAGE_ID_USER_LIST)) {
            Message message = new MessageUserList(address, messageSystemContext.getDbAddress(),
                    errorMessage, userFoundedById, userId, address, userDataSetServlet);
            messageSystemContext.getMessageSystem().sendMessage(message);
        }
    }

    public void setUserFoundedById(UserDataSetServlet userDataSetServlet, String userFoundedById) {
        userDataSetServlet.setUserFoundedById(userFoundedById);
    }

    public void setErrorMessage(UserDataSetServlet userDataSetServlet, String errorMessage) {
        userDataSetServlet.setErrorMessage(errorMessage);
    }

    public void showPage(UserDataSetServlet userDataSetServlet, List<UserDataSetDto> userListDto, String errorMessage, String userFoundedById, long userId) {
        userDataSetServlet.setUserList(userListDto);
        userDataSetServlet.setErrorMessage(errorMessage);
        userDataSetServlet.setUserFoundedById(userFoundedById);
        userDataSetServlet.setUserId(userId);
    }

    public void queryPut() {
        linkedBlockingQueue.offer(1);
    }

    public void queryTake() {
        try {
            linkedBlockingQueue.take();
        } catch (InterruptedException e) {
            logger.log(Level.INFO, "FrontEndService interrupted.");
        }
    }

}
