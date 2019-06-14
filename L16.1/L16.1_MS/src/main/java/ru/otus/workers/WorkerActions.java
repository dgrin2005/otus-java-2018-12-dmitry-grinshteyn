package ru.otus.workers;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class WorkerActions {

    private final Map <String, Consumer> actionMap;

    public WorkerActions() {
        this.actionMap = new HashMap<>();
    }

    public Map<String, Consumer> getActionMap() {
        return actionMap;
    }

    public void addAction(String name, Consumer consumer) {
        actionMap.put(name, consumer);
    }

}
