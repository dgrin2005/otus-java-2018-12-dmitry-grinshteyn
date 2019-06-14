package ru.otus.FrontEnd;

import ru.otus.FEWorker.Actions.ActionShowPage;
import ru.otus.FEWorker.ClientSocketMessageWorker;
import ru.otus.DataSet.PhoneDataSet;
import ru.otus.DataSet.UserDataSet;
import ru.otus.FEWorker.FEServiceCallable;
import ru.otus.MessageDto;
import ru.otus.exception.MyMSException;
import ru.otus.messages.Address;
import ru.otus.messages.DBMessage;
import ru.otus.messages.FEMessage;
import ru.otus.messages.Message;
import ru.otus.workers.SocketMessageWorker;
import ru.otus.workers.WorkerActions;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static ru.otus.ServerMain.*;
import static ru.otus.messages.Message.*;

public class FrontEndService {

    private final static Logger logger = Logger.getLogger(FrontEndService.class.getName());
    private SocketMessageWorker client;
    private final ConcurrentHashMap<UUID, LinkedBlockingQueue<Message>> uuidLinkedBlockingQueueConcurrentHashMap;

    public FrontEndService() {
        this.uuidLinkedBlockingQueueConcurrentHashMap = new ConcurrentHashMap<>();
    }

    private void start() throws InterruptedException, IOException, MyMSException {
        client = new ClientSocketMessageWorker(HOST, PORT_MS);
        client.init();
        logger.log(Level.INFO, "Start FE client");
        ExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        WorkerActions feServiceActions = new WorkerActions();
        feServiceActions.addAction(MESSAGE_ID_SHOW_PAGE, new ActionShowPage());
        Callable<Integer> callable = new FEServiceCallable(this, client, feServiceActions);
        executorService.submit(callable);
        executorService.shutdown();
    }

    private void destroy() throws IOException, MyMSException {
        client.close();
    }

    public Message sendMessage(String messageId, UserDataSet userDataSet, UUID uuid) {
        if (messageId.equals(MESSAGE_ID_CREATE_NEW_USER)) {
            Message message = new FEMessage(client.getAddress(), new Address(DBMessage.class.getName()), MESSAGE_ID_CREATE_NEW_USER,
                    uuid,
                    new MessageDto(userDataSet.getId(),
                            userDataSet.getName(),
                            userDataSet.getAge(),
                            userDataSet.getAddress().getStreet(),
                            userDataSet.getPhones().stream()
                                    .map(PhoneDataSet::getNumber)
                                    .collect(Collectors.joining(", ")),
                            ""));
            return sendAndWaitMessage(message, uuid);
        }
        return null;
    }

    public Message sendMessage(String messageId, UUID uuid) {
        if (messageId.equals(MESSAGE_ID_USER_LIST)) {
            Message message = new FEMessage(client.getAddress(), new Address(DBMessage.class.getName()), MESSAGE_ID_USER_LIST,
                    uuid,
                    new MessageDto(""));
            return sendAndWaitMessage(message, uuid);
        }
        return null;
    }

    public Message sendMessage(String messageId, long userId, UUID uuid) {
        if (messageId.equals(MESSAGE_ID_DELETE_USER)) {
            Message message = new FEMessage(client.getAddress(), new Address(DBMessage.class.getName()), MESSAGE_ID_DELETE_USER,
                    uuid,
                    new MessageDto(userId, "", ""));
            return sendAndWaitMessage(message, uuid);
        }
        if (messageId.equals(MESSAGE_ID_FIND_USER)) {
            Message message = new FEMessage(client.getAddress(), new Address(DBMessage.class.getName()), MESSAGE_ID_FIND_USER,
                    uuid,
                    new MessageDto(userId,"", ""));
            return sendAndWaitMessage(message, uuid);
        }
        return null;
    }

    private void putNewUuidToMap(UUID uuid) {
        LinkedBlockingQueue<Message> queue;
        if (!uuidLinkedBlockingQueueConcurrentHashMap.containsKey(uuid)) {
            queue = new LinkedBlockingQueue<>();
            uuidLinkedBlockingQueueConcurrentHashMap.put(uuid, queue);
        }
    }

    private Message sendAndWaitMessage(Message message, UUID uuid) {
        putNewUuidToMap(uuid);
        client.send(message);
        logger.log(Level.INFO, "FE Message send: " + message.toString());
        return queryTake(uuid);
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
