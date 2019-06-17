package ru.otus.workers;

import ru.otus.exception.MyMSException;
import ru.otus.messages.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class WorkerActions {

    private final Map <String, Consumer> actionMap;

    public WorkerActions() {
        this.actionMap = new HashMap<>();
    }

    public Optional<Consumer> getAction(Message message) {
        return Optional.ofNullable(actionMap.get(message.getMessageId()));
    }

    public void addAction(String name, Consumer consumer) {
        actionMap.put(name, consumer);
    }

}
