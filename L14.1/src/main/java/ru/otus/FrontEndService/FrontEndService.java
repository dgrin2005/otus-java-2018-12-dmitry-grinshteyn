package ru.otus.FrontEndService;

import ru.otus.DataSet.UserDataSet;
import ru.otus.MessageSystem.*;
import ru.otus.MessageSystem.Messages.DB.*;
import ru.otus.WebServer.Dto.UserDataSetDto;
import ru.otus.WebServer.UserDataSetServlet;

import javax.servlet.http.HttpServlet;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FrontEndService implements Addressee {

    private final static Logger logger = Logger.getLogger(FrontEndService.class.getName());

    public final static String MESSAGE_ID_CREATE_NEW_USER = "CreateNewUser";
    public final static String MESSAGE_ID_DELETE_USER = "DeleteUser";
    public final static String MESSAGE_ID_FIND_USER = "FindUser";
    public final static String MESSAGE_ID_USER_LIST = "UserList";

    public final static String MESSAGE_ID_SET_USER_FOUNDED_BY_ID = "SetUserFoundedById";
    public final static String MESSAGE_ID_SET_ERROR_MESSAGE = "SetErrorMessage";
    public final static String MESSAGE_ID_SHOW_PAGE = "ShowPage";

    private final MessageSystemContext messageSystemContext;
    private final Address address;

    private HttpServlet[] httpServlets;
    private final ConcurrentHashMap<UUID, LinkedBlockingQueue<Integer>> uuidLinkedBlockingQueueConcurrentHashMap;

    public FrontEndService(MessageSystemContext messageSystemContext, Address address) {
        this.messageSystemContext = messageSystemContext;
        this.address = address;
        this.messageSystemContext.getMessageSystem().addAddressee(this);
        this.uuidLinkedBlockingQueueConcurrentHashMap = new ConcurrentHashMap<>();
    }

    public void setHttpServlets(HttpServlet... httpServlets) {
        this.httpServlets = httpServlets;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public MessageSystem getMessageSystem() {
        return messageSystemContext.getMessageSystem();
    }

    public void sendMessage(String messageId, UserDataSet userDataSet, UUID uuid) {
        if (messageId.equals(MESSAGE_ID_CREATE_NEW_USER)) {
            Message message = new MessageCreateNewUser(address, messageSystemContext.getDbAddress(), userDataSet, uuid);
            messageSystemContext.getMessageSystem().sendMessage(message);
        }
    }

    public void sendMessage(String messageId,
                            String errorMessage, String userFoundedById, long userId, UUID uuid) {
        if (messageId.equals(MESSAGE_ID_DELETE_USER)) {
            Message message = new MessageDeleteById(address, messageSystemContext.getDbAddress(), userId, uuid);
            messageSystemContext.getMessageSystem().sendMessage(message);
        }
        if (messageId.equals(MESSAGE_ID_FIND_USER)) {
            Message message = new MessageFindUser(address, messageSystemContext.getDbAddress(), userId, uuid);
            messageSystemContext.getMessageSystem().sendMessage(message);
        }
        if (messageId.equals(MESSAGE_ID_USER_LIST)) {
            Message message = new MessageUserList(address, messageSystemContext.getDbAddress(),
                    errorMessage, userFoundedById, userId, address, uuid);
            messageSystemContext.getMessageSystem().sendMessage(message);
        }
    }

    public void handleMessage(String messageId,
                              List<UserDataSetDto> userListDto, String errorMessage, String userFoundedById, long userId) {
        if (messageId.equals(MESSAGE_ID_SHOW_PAGE)) {
            ((UserDataSetServlet)httpServlets[0]).showPage(userListDto, errorMessage, userFoundedById, userId);
        }
    }

    public void handleMessage(String messageId,
                              String messageString) {
        if (messageId.equals(MESSAGE_ID_SET_ERROR_MESSAGE)) {
            ((UserDataSetServlet)httpServlets[0]).setErrorMessage(messageString);
        }
        if (messageId.equals(MESSAGE_ID_SET_USER_FOUNDED_BY_ID)) {
            ((UserDataSetServlet)httpServlets[0]).setUserFoundedById(messageString);
        }
    }

    public void queryPut(UUID uuid) {
        uuidLinkedBlockingQueueConcurrentHashMap.compute(uuid, (k, v) -> new LinkedBlockingQueue<>(1)).add(1);
    }

    public void queryTake(UUID uuid) {
        LinkedBlockingQueue<Integer> queue = null;
        while (queue == null) {
            queue = uuidLinkedBlockingQueueConcurrentHashMap.computeIfPresent(uuid, (k, v) -> v);
        }
        try {
            queue.take();
        } catch (InterruptedException e) {
            logger.log(Level.INFO, "FrontEndService interrupted.");
        }
    }

}
