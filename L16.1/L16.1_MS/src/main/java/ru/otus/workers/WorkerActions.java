package ru.otus.workers;

import ru.otus.exception.MyMSException;
import ru.otus.messages.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class WorkerActions {

    private final Map <String, Consumer> actionMap;

    public WorkerActions() {
        this.actionMap = new HashMap<>();
    }

    public Consumer getAction(Message message) throws MyMSException {
        if (actionMap.containsKey(message.getMessageId())) {
            return actionMap.get(message.getMessageId());
        } else {
            throw new MyMSException("Action not found for message " + message);
        }
    }

    public void addAction(String name, Consumer consumer) {
        actionMap.put(name, consumer);
    }

}
