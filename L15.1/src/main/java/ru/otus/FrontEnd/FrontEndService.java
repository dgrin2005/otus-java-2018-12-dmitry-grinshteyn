package ru.otus.FrontEnd;

import ru.otus.DataSet.UserDataSet;
import ru.otus.MessageSystem.*;
import ru.otus.MessageSystem.Messages.DB.*;

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

    private final MessageSystemContext messageSystemContext;
    private final Address address;

    private final ConcurrentHashMap<UUID, LinkedBlockingQueue<Message>> uuidLinkedBlockingQueueConcurrentHashMap;

    public FrontEndService(MessageSystemContext messageSystemContext, Address address) {
        this.messageSystemContext = messageSystemContext;
        this.address = address;
        this.messageSystemContext.getMessageSystem().addAddressee(this);
        this.messageSystemContext.getMessageSystem().startThread(this);
        this.uuidLinkedBlockingQueueConcurrentHashMap = new ConcurrentHashMap<>();
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public MessageSystem getMessageSystem() {
        return messageSystemContext.getMessageSystem();
    }

    public Message sendMessage(String messageId, UserDataSet userDataSet, UUID uuid) {
        if (messageId.equals(MESSAGE_ID_CREATE_NEW_USER)) {
            Message message = new MessageCreateNewUser(address, messageSystemContext.getDbAddress(), userDataSet, uuid);
            return sendAndWaitMessage(message, uuid);
        }
        return null;
    }

    public Message sendMessage(String messageId, UUID uuid) {
        if (messageId.equals(MESSAGE_ID_USER_LIST)) {
            Message message = new MessageUserList(address, messageSystemContext.getDbAddress(), address, uuid);
            return sendAndWaitMessage(message, uuid);
        }
        return null;
    }

    public Message sendMessage(String messageId, long userId, UUID uuid) {
        if (messageId.equals(MESSAGE_ID_DELETE_USER)) {
            Message message = new MessageDeleteById(address, messageSystemContext.getDbAddress(), userId, uuid);
            return sendAndWaitMessage(message, uuid);
        }
        if (messageId.equals(MESSAGE_ID_FIND_USER)) {
            Message message = new MessageFindUser(address, messageSystemContext.getDbAddress(), userId, uuid);
            return sendAndWaitMessage(message, uuid);
        }
        return null;
    }

    private Message sendAndWaitMessage(Message message, UUID uuid) {
        putNewUuidToMap(uuid);
        messageSystemContext.getMessageSystem().sendMessage(message);
        return queryTake(uuid);
    }

    private void putNewUuidToMap(UUID uuid) {
        LinkedBlockingQueue<Message> queue;
        if (!uuidLinkedBlockingQueueConcurrentHashMap.containsKey(uuid)) {
            queue = new LinkedBlockingQueue<>();
            uuidLinkedBlockingQueueConcurrentHashMap.put(uuid, queue);
        }
    }

    public void queryPut(UUID uuid, Message message) {
        LinkedBlockingQueue<Message> queue;
        if (uuidLinkedBlockingQueueConcurrentHashMap.containsKey(uuid)) {
            queue = uuidLinkedBlockingQueueConcurrentHashMap.get(uuid);
        } else {
            queue = new LinkedBlockingQueue<>();
        }
        queue.add(message);
        uuidLinkedBlockingQueueConcurrentHashMap.put(uuid, queue);
    }

    private Message queryTake(UUID uuid) {
        LinkedBlockingQueue <Message> queue = uuidLinkedBlockingQueueConcurrentHashMap.computeIfPresent(uuid, (k, v) -> v);
        Message message = null;
        try {
            message = Objects.requireNonNull(queue).take();
        } catch (InterruptedException e) {
            logger.log(Level.INFO, "FrontEndService interrupted.");
        }
        return message;
    }
}
