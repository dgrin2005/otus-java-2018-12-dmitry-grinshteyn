package ru.otus.MessageSystem;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageSystem {

    private final static Logger logger = Logger.getLogger(MessageSystem.class.getName());

    private final List<Thread> addresseeThreadList;
    private final Map<Address, LinkedBlockingQueue<Message>> messagesMap;
    private final Map<Address, Addressee> addresseeMap;

    public MessageSystem() {
        addresseeThreadList = new ArrayList<>();
        messagesMap = new HashMap<>();
        addresseeMap = new HashMap<>();
    }

    public void addAddressee(Addressee addressee) {
        addresseeMap.put(addressee.getAddress(), addressee);
        messagesMap.put(addressee.getAddress(), new LinkedBlockingQueue<>());
    }

    public void sendMessage(Message message) {
        try {
            messagesMap.get(message.getTo()).put(message);
        } catch (InterruptedException e) {
            logger.log(Level.INFO, e.getMessage());
        }
    }

    public void start() {
        for (Map.Entry<Address, Addressee> entry : addresseeMap.entrySet()) {
            String name = "MS-addressee-" + entry.getKey().getId();
            Thread thread = new Thread(() -> {
                while (true) {
                    LinkedBlockingQueue<Message> queue = messagesMap.get(entry.getKey());
                    while (true) {
                        try {
                            Message message = queue.take();
                            logger.log(Level.INFO, "Thread " + name + " take  message: " + message);
                            message.exec(entry.getValue());
                        } catch (Exception e) {
                            logger.log(Level.INFO, e.getMessage());
                            logger.log(Level.INFO, "Thread" + name +" interrupted.");
                            return;
                        }
                    }
                }
            });
            thread.setName(name);
            thread.start();
            addresseeThreadList.add(thread);
        }
    }

    public void stop() {
        addresseeThreadList.forEach(Thread::interrupt);
    }
}
